package core;

import java.util.ArrayList;

import math.Ray;
import math.Vector3f;
import prim.EngineObject;
import prim.Intersection;
import prim.Light;
import prim.Material;
import scn.Scene;

public class Renderer
{
	private int m_width;
	private int m_height;
	private int m_samples;
	private int m_maxDepth;
	
	private float m_heightWidthRatio;
	private float m_pixelSize;
	private float m_imageLeft;
	private float m_imageTop;
	private float m_halfTanFOV = (float) Math.tan(35f * (float) Math.PI / 180f);

	private Scene m_scene;

	public Renderer(int width, int height, int samples, int maxDepth, Scene scene)
	{
		m_width = width;
		m_height = height;
		m_samples = samples;
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
		for (int i = 0; i < m_samples; i++)
		{
			Vector3f sample = new Vector3f(randomFloat(left, right), randomFloat(bottom, top), 1f);
			color.add(traceRay(new Ray(Vector3f.zero(), sample), 0));
		}

		return color.mul(1f / (float) m_samples); 
	}

	private float randomFloat(float low, float high)
	{
		return low + (high - low) * (float) Math.random();
	}

	private Vector3f traceRay(Ray ray, int depth)
	{
		ArrayList<EngineObject> objects = m_scene.getObjects();
		ArrayList<Light> lights = m_scene.getLights();

		Intersection min = getIntersection(ray, objects);

		if (min.isIntersect())
		{
			Vector3f inter = ray.getPoint(min.getDistance());
			Vector3f color = shadePoint(inter, min, objects, lights);

			if (depth < m_maxDepth && min.getMaterial().isReflective())
			{
				Vector3f reflected = traceRay(new Ray(inter, min.getNormal().reflect(ray.getDirection()).mul(-1f)), depth + 1);
				return color.mul(0.4f).add(reflected.getMul(0.6f));
			}
			else
			{
				return color;
			}
		}
		else
		{
			return m_scene.getSkyColor();
		}
	}

	private Intersection getIntersection(Ray ray, ArrayList<EngineObject> objects)
	{
		float dist = Float.MAX_VALUE;
		Vector3f normal = Vector3f.zero();
		Material material = new Material(Vector3f.zero(), 0, Vector3f.zero(), false);
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

		if (intersect)
		{
			return new Intersection(dist, normal, material, index);
		}
		else
		{
			return new Intersection();
		}
	}

	private Vector3f shadePoint(Vector3f point, Intersection intersection, ArrayList<EngineObject> objects, ArrayList<Light> lights)
	{
		Material material = intersection.getMaterial();

		Vector3f totalDiffuse = Vector3f.zero();
		Vector3f totalSpecular = Vector3f.zero();

		for (int i = 0; i < lights.size(); i++)
		{
			Light light = lights.get(i);
			Ray lightRay = new Ray(point, light.getPosition().getSub(point));

			if (!isPointShadowed(lightRay, objects))
			{
				totalDiffuse.add(getDiffuse(light, intersection.getNormal(), point));
				totalSpecular.add(getSpecular(light, intersection.getNormal(), point, Vector3f.zero(), material.getSpecularPower()));
			}
		}

		Vector3f objectColor = material.getColor();
		Vector3f ambient = getAmbient(objectColor).mul(material.getComponent(Material.COMPONENT_AMBIENT));
		ambient.add(totalSpecular.mul(material.getComponent(Material.COMPONENT_SPECULAR)));
		totalDiffuse.mul(material.getComponent(Material.COMPONENT_DIFFUSE));

		return new Vector3f(Math.min(255f, objectColor.getX() * totalDiffuse.getX() + ambient.getX()),
							Math.min(255f, objectColor.getY() * totalDiffuse.getY() + ambient.getY()),
							Math.min(255f, objectColor.getZ() * totalDiffuse.getZ() + ambient.getZ()));

	}

	private boolean isPointShadowed(Ray ray, ArrayList<EngineObject> objects)
	{
		Ray newRay = new Ray(ray.getPoint(0.001f), ray.getDirection());

		for (int i = 0; i < objects.size(); i++)
		{
			if (objects.get(i).intersect(newRay).isIntersect())
			{
				return true;
			}
		}

		return false;
	}

	private Vector3f getAmbient(Vector3f color)
	{
		return color.getAdd(m_scene.getSkyColor().getMul(0.01f));
		// return color.getMul(0.4f);

	}

	private Vector3f getDiffuse(Light light, Vector3f normal, Vector3f point)
	{
		Vector3f pointToLight = light.getPosition().getSub(point);
		float distance = pointToLight.length();
		float cos = Math.max(0f, pointToLight.dot(normal) / distance);
		float intensity = Math.min(1f / cos, light.getRadius() / (distance * distance));
		return light.getColor().getMul((cos * intensity) / 255f);
	}

	private Vector3f getSpecular(Light light, Vector3f normal, Vector3f point, Vector3f eye, int power)
	{
		Vector3f lightPosition = light.getPosition();
		Vector3f pointToLight = lightPosition.getSub(point);
		Vector3f pointToEye = eye.getSub(point);
		Vector3f reflected = normal.reflect(pointToLight);
		float cos = Math.max(0f, pointToEye.dot(reflected) / (pointToEye.length() * reflected.length()));
		float distance = pointToLight.length();
		return light.getColor().getMul((float) Math.pow(cos, power)).mul(light.getRadius() / (distance * distance));
	}
}