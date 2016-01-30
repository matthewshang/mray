package prim;

import java.util.ArrayList;

import math.AABB;
import math.Matrix4f;
import math.Quaternion;
import math.Ray;
import math.Vector3f;
import matl.Material;

public class GeometryInstance implements EngineObject
{
	private GeometryData m_geometry;
	private Material m_material;

	private Matrix4f m_transform;
	private Matrix4f m_transformTranspose;
	private Matrix4f m_inverseTransform;
	private Matrix4f m_inverseTranspose;
	private AABB m_boundingBox;

	public GeometryInstance(Vector3f position, Vector3f scale, Quaternion rotation, GeometryData geometry, Material material)
	{
		m_geometry = geometry;
		m_material = material;

		Matrix4f s = new Matrix4f();
		s.initScale(scale);
		Matrix4f rot = new Matrix4f(rotation);
		m_transform = s.multiply(rot);
		m_transform.initTranslate(position);

		m_inverseTransform = m_transform.getInverse();
		m_transformTranspose = m_transform.getTranspose();
		m_inverseTranspose = m_inverseTransform.getTranspose();

		Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		Vector3f max = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
		Vector3f[] boxVertices = m_geometry.getBoundingBox().getVertices();
		for (int i = 0; i < 8; i++)
		{
			m_transform.apply(boxVertices[i], 1f).sortMinAndMax(min, max);
		}

		m_boundingBox = new AABB(min, max);
	}

	public AABB getBounds()
	{
		return m_boundingBox;
	}

	public Intersection intersect(Ray ray)
	{
		if (m_boundingBox.intersect(ray) == 0f)
		{
			return new Intersection();
		}

		Ray newRay = new Ray(m_inverseTransform.apply(ray.getOrigin(), 1f), m_transformTranspose.apply(ray.getDirection(), 0f));

		Intersection min = m_geometry.intersect(newRay);

		if (min.isIntersect())
		{
			float d = min.getDistance();
			Vector3f p = m_transform.apply(newRay.getPoint(d), 1f);
			d = ray.getOrigin().getSub(p).length();
			if (d < 0f)
			{
				return new Intersection();
			}
			min.setDistance(d);
			min.setNormal(m_inverseTranspose.apply(min.getNormal(), 0f).normalize());
			min.setMaterial(m_material);

			return min;
		}
		else
		{
			return new Intersection();
		}
	}
}