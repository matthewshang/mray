import java.util.ArrayList;

public class Scene
{
	private ArrayList<EngineObject> m_objects;
	private ArrayList<Light> m_lights;
	private static Vector3f skyColor = new Vector3f(153.0f, 204.0f, 255.0f);

	public Scene()
	{
		m_objects = new ArrayList<EngineObject>();
		m_lights = new ArrayList<Light>();
	}

	public void addObject(EngineObject object)
	{
		m_objects.add(object);
	}

	public void addLight(Light light)
	{
		m_lights.add(light);
	}

	public Vector3f traceRay(Ray ray)
	{
		Intersection min = getIntersection(ray, -1);

		if (min.isIntersect())
		{
			Vector3f inter = ray.getPoint(min.getDistance());
			// System.out.println(inter.toString());

			Vector3f intensities = new Vector3f(0.0f, 0.0f, 0.0f);

			for (int i = 0; i < m_lights.size(); i++)
			{
				Light light = m_lights.get(i);
				Ray lightRay = new Ray(inter, light.getPosition().getSub(inter));
				Intersection toLight = getIntersection(lightRay, min.getIndex());

				if (toLight.getDistance() < 0.0f)
				{
					Vector3f diffuse = getDiffuse(light, min.getNormal(), inter);
					intensities.add(diffuse);
					// intensities.add(getDiffuse(light, min.getNormal(), inter));

				}
			}

			Vector3f ambient = getAmbient(min.getColor());
			Vector3f objectColor = min.getColor();
			return new Vector3f(Math.min(255.0f, objectColor.getX() * intensities.getX() + ambient.getX()),
								Math.min(255.0f, objectColor.getY() * intensities.getY() + ambient.getY()),
								Math.min(255.0f, objectColor.getZ() * intensities.getZ() + ambient.getZ()));
		}
		else
		{
			return skyColor;
		}
	}

	private Intersection getIntersection(Ray ray, int object)
	{
		Intersection min = new Intersection(Float.MAX_VALUE, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
		boolean intersect = false;

		for (int i = 0; i < m_objects.size(); i++)
		{
			Intersection inter = m_objects.get(i).intersect(ray);
			if (inter.isIntersect() && inter.getDistance() < min.getDistance() && object != i)
			{
				min = inter;
				min.setIndex(i);
				intersect = true;
			}
		}

		if (intersect)
		{
			return min;
		}
		else
		{
			return new Intersection();
		}
	}

	private Vector3f getAmbient(Vector3f intensities)
	{
		return intensities.getMul(0.0001f);
	}

	private Vector3f getDiffuse(Light light, Vector3f normal, Vector3f point)
	{
		Vector3f pointToLight = light.getPosition().getSub(point);
		float distance = pointToLight.length();
		float cos = Math.max(0.0f, pointToLight.dot(normal) / distance);
		float intensity = 4.0f / distance;
		return light.getColor().getMul((cos * intensity) / 255.0f);
	}
}