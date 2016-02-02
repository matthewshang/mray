package core;

import java.util.ArrayList;

import light.Light;
import math.Ray;
import math.Sampler;
import math.Vector3f;
import matl.Material;
import prim.EngineObject;
import prim.Intersection;
import scn.Scene;

public class Renderer
{
	private int m_width;
	private int m_height;
	private int m_pixelSamples;
	private int m_maxDepth;
	
	private float m_heightWidthRatio;
	private float m_pixelSize;
	private float m_imageLeft;
	private float m_imageTop;
	private float m_halfTanFOV = (float) Math.tan(35f * (float) Math.PI / 180f);

	private Scene m_scene;

	public Renderer(int width, int height, int pixelSamples, int maxDepth, Scene scene)
	{
		m_width = width;
		m_height = height;
		m_pixelSamples = pixelSamples;
		m_maxDepth = maxDepth;

		m_heightWidthRatio = (float) m_height / (float) m_width;
		m_pixelSize = m_halfTanFOV * 2f / (float) m_width;
		m_scene = scene;
		m_imageLeft = -1f * m_halfTanFOV;
		m_imageTop = m_heightWidthRatio * m_halfTanFOV;
	}

	public RenderChunk[] getImageChunks(int chunkWidth, int chunkHeight)
	{
		int columns = m_width / chunkWidth;
		int rows = m_height / chunkHeight;
		RenderChunk[] chunks = new RenderChunk[columns * rows];
		for (int row = 0; row < rows; row++)
		{
			for (int column = 0; column < columns; column++)
			{
				chunks[row * columns + column] = new RenderChunk(chunkWidth * column, chunkWidth * (column + 1),
																 chunkHeight * row, chunkHeight * (row + 1));
			}
		}

		return chunks;
	}

	public void traceChunk(RenderChunk chunk)
	{
		for (int y = chunk.getStartY(); y < chunk.getEndY(); y++)
		{
			for (int x = chunk.getStartX(); x < chunk.getEndX(); x++)
			{
				Vector3f color = samplePixel(x, y);
				chunk.setAt(x, y, (int) color.getX() << 16 | (int) color.getY() << 8 | (int) color.getZ());
			}
		}
	}

	private Vector3f samplePixel(int imageX, int imageY)
	{
		float left = m_imageLeft + m_pixelSize * imageX;
		float right = left + m_pixelSize;
		float top = m_imageTop - m_pixelSize * imageY;
		float bottom = top - m_pixelSize;

		Vector3f color = Vector3f.zero();
		for (int i = 0; i < m_pixelSamples; i++)
		{
			Vector3f sample = new Vector3f(randomFloat(left, right), randomFloat(bottom, top), 1f);
			Vector3f rayColor = traceRay(new Ray(new Vector3f(0f, 0f, 0f), sample), 0); 
			color.add(rayColor);
		}

		return color.mul(1f / (float) m_pixelSamples); 
	}

	private float randomFloat(float low, float high)
	{
		return low + (high - low) * (float) Math.random();
	}

	public Vector3f traceRay(Ray ray, int depth)
	{
		if (depth > m_maxDepth)
		{
			return Vector3f.zero();
		}

		ArrayList<EngineObject> objects = m_scene.getObjects();
		ArrayList<Light> lights = m_scene.getLights();

		Intersection min = getIntersection(ray, objects);
		Intersection lightMin = getLightIntersection(ray, lights);

		if (min.getDistance() > lightMin.getDistance())
		{
			if (lightMin.isIntersect())
			{
				// lightMin.normal is set to lightColor
				return lightMin.getNormal();
			}
		}

		if (min.isIntersect())
		{
			Vector3f position = ray.getPoint(min.getDistance());
			Vector3f normal = min.getNormal();
			Material material = min.getMaterial();
			
			Vector3f surfaceColor = material.getColor().getMul(1f / 255f);
			float random = (float) Math.random();
			Vector3f direct; 
			if (material.receivesDirect(random))
			{
				direct = directLighting(position, normal, lights);
			}
			else 
			{
				direct = Vector3f.zero();
			}
			Vector3f sample = material.sample(normal, ray.getDirection(), random);
			Vector3f indirect = traceRay(new Ray(position, sample), depth + 1).getMul(Math.max(0f, normal.dot(sample)));
			Vector3f color = surfaceColor.mul(direct.add(indirect.mul(material.getIndirectAmount(random))));
			if (depth == 0)
			{
				color.pow(1f / 2.2f);
				Vector3f clamped = new Vector3f(Math.min(1f, color.getX()), Math.min(1f, color.getY()), Math.min(1f, color.getZ()));
				return clamped.mul(255f);
			}
			else
			{
				return color;
			}
		}
		else
		{
			if (depth == 0)
			{
				return m_scene.getSkyColor();
			}
			else
			{
				return m_scene.getSkyColor().getMul(1f / 255f);
			}
		}
	}

	private Vector3f directLighting(Vector3f position, Vector3f normal, ArrayList<Light> lights)
	{
		Vector3f diffuse = Vector3f.zero();

		Vector3f skySample = Sampler.sampleSphere(new Vector3f(0f, 1f, 0f), 1000f, 0f, (float) Math.PI / 2f);
		Ray skyRay = new Ray(position, skySample.sub(position));
		if (!isPointShadowed(skyRay))
		{
			diffuse.add(m_scene.getSkyColor().getMul((Math.max(0f, skySample.normalize().dot(normal))) / 255f));
		}

		for (int i = 0; i < lights.size(); i++)
		{
			Vector3f sample = lights.get(i).getSample(position);
			if (!isPointShadowed(new Ray(position, sample.getSub(position))))
			{
				diffuse.add(diffuseLighting(lights.get(i), sample, position, normal));
			}
		}

		return diffuse;
	}

	private Vector3f diffuseLighting(Light light, Vector3f lightPosition, Vector3f position, Vector3f normal)
	{
		Vector3f posToLight = lightPosition.getSub(position);
		float distance = posToLight.length();
		float cos = Math.max(0f, posToLight.dot(normal) / distance);
		float intensity = Math.min(1f / cos, light.getPower() / (distance * distance));
		return light.getColor().getMul((cos * intensity) / 255f);
	}

	private Intersection getIntersection(Ray ray, ArrayList<EngineObject> objects)
	{
		float dist = Float.MAX_VALUE;
		Vector3f normal = Vector3f.zero();
		Material material = null;
		int index = -1;
		boolean intersect = false;

		Ray newRay = new Ray(ray.getPoint(0.001f), ray.getDirection());

		for (int i = 0; i < objects.size(); i++)
		{
			Intersection inter = objects.get(i).intersect(newRay);
			if (inter.isIntersect() && inter.getDistance() < dist)
			{
				dist = inter.getDistance();
				index = i;
				normal = inter.getNormal();
				material = inter.getMaterial();
				intersect = true;
			}
		}

		if (intersect && material != null)
		{
			return new Intersection(dist, normal, material, index);
		}
		else
		{
			return new Intersection();
		}
	}

	private Intersection getLightIntersection(Ray ray, ArrayList<Light> lights)
	{
		float dist = Float.MAX_VALUE;
		Vector3f color = Vector3f.zero();
		boolean intersect = false;

		Ray newRay = new Ray(ray.getPoint(0.001f), ray.getDirection());

		for (int i = 0; i < lights.size(); i++)
		{
			Intersection inter = lights.get(i).intersect(ray);
			if (inter.isIntersect() && inter.getDistance() < dist)
			{
				dist = inter.getDistance();
				color = lights.get(i).getColor();
				intersect = true;
			}
		}

		if (intersect)
		{
			return new Intersection(dist, color, null, -1);
		}
		else
		{
			return new Intersection();
		}
	}

	public boolean isPointShadowed(Ray ray)
	{
		Ray newRay = new Ray(ray.getPoint(0.001f), ray.getDirection());
		ArrayList<EngineObject> objects = m_scene.getObjects();

		for (int i = 0; i < objects.size(); i++)
		{
			if (objects.get(i).intersect(newRay).isIntersect())
			{
				return true;
			}
		}

		return false;
	}
}