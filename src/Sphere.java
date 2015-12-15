public class Sphere implements EngineObject
{
	private Vector3f m_center;
	private float m_radius;
	private Vector3f m_color;

	public Sphere(Vector3f center, float radius, Vector3f color)
	{
		m_center = center;
		m_radius = radius;
		m_color = color;
	}

	public Intersection intersect(Ray ray)
	{
		Vector3f l = ray.getDirection();
		Vector3f o = ray.getOrigin();
		float a = l.dot(o.getSub(m_center));
		Vector3f b = m_center.getSub(o);
		float discrim = a * a - (b.dot(b) - m_radius * m_radius);

		if (discrim < 0.0f)
		{
			return new Intersection();
		}

		float out = -1 * l.dot(o.getSub(m_center));
		if (discrim == 0.0f)
		{
			return new Intersection(out, ray.getNormal(out, m_center), m_color, -1);
		}
		discrim = (float) Math.sqrt(discrim);
		float dp = out + discrim;
		float dm = out - discrim;

		if (dp > 0.0f && dm > 0.0f)
		{
			if (dm < dp)
			{
				return new Intersection(dm, ray.getNormal(dm, m_center), m_color, -1);
			}
			else
			{
				return new Intersection(dp, ray.getNormal(dp, m_center), m_color, -1);
			}
		}
		else
		{
			if (dp < 0.0f && dm < 0.0f)
			{
				return new Intersection();
			}
			else if (dp < 0.0f)
			{
				return new Intersection(dm, ray.getNormal(dm, m_center), m_color, -1);
			}
			else if (dm < 0.0f)
			{
				return new Intersection(dp, ray.getNormal(dp, m_center), m_color, -1);
			}

			return new Intersection();
		}
	}
}