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
		Intersection minIntersection = new Intersection(Float.MAX_VALUE, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
		boolean intersect = false;

		for (int i = 0; i < m_objects.size(); i++)
		{
			Intersection inter = m_objects.get(i).intersect(ray);
			if (inter.isIntersect() && inter.getDistance() < minIntersection.getDistance())
			{
				minIntersection = inter;
				intersect = true;
			}
		}

		if (intersect)
		{
			Vector3f inter = ray.getOrigin().getAdd(ray.getDirection().getMul(minIntersection.getDistance()));

			Vector3f color = new Vector3f(0.0f, 0.0f, 0.0f);

			for (int i = 0; i < m_lights.size(); i++)
			{
				Light light = m_lights.get(i);
				Vector3f interLight = light.getPosition().getSub(inter);
				float cos = interLight.dot(minIntersection.getNormal()) / interLight.length();
				cos = Math.max(0.0f, cos);
				color.add(light.getColor().getMul(cos / 255.0f));
			}

			Vector3f objectColor = minIntersection.getColor();
			return new Vector3f(objectColor.getX() * color.getX(),
								objectColor.getY() * color.getY(),
								objectColor.getZ() * color.getZ());
		}
		else
		{
			return skyColor;

		}
	}
}