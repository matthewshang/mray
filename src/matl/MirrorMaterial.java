package matl;

import java.util.ArrayList;

import core.Renderer;
import math.Vector3f;
import math.Ray;
import prim.Light;

public class MirrorMaterial implements Material
{
	private Vector3f m_color;
	private Vector3f m_tint;
	private float m_reflectedAmount;
	private float m_diffuseAmount;

	public MirrorMaterial(Vector3f color, Vector3f tint, float reflectedAmount, float diffuseAmount)
	{
		m_color = color;
		m_tint = tint;
		m_reflectedAmount = reflectedAmount;
		m_diffuseAmount = diffuseAmount;
	}

	public Vector3f shadePoint(Renderer renderer, Vector3f rayDirection, int depth, Vector3f point, Vector3f eye, Vector3f normal, ArrayList<Light> lights)
	{
		Vector3f totalDiffuse = Vector3f.zero();

		if (m_diffuseAmount > 0f)
		{
			for (int i = 0; i < lights.size(); i++)
			{
				Light light = lights.get(i);
				Ray lightRay = new Ray(point, light.getPosition().getSub(point));

				if (!renderer.isPointShadowed(lightRay))
				{
					totalDiffuse.add(getDiffuse(light, normal, point));
				}
			}
		}

		Vector3f reflected = normal.reflect(rayDirection).mul(-1f);
		Vector3f reflectedColor = renderer.traceRay(new Ray(reflected.getMul(0.001f).add(point), reflected), depth + 1);
		return reflectedColor.getMulComponents(m_tint).mul(m_reflectedAmount).add(m_color.getMulComponents(totalDiffuse.mul(m_diffuseAmount)));
	}

	private Vector3f getDiffuse(Light light, Vector3f normal, Vector3f point)
	{
		Vector3f pointToLight = light.getPosition().getSub(point);
		float distance = pointToLight.length();
		float cos = Math.max(0f, pointToLight.dot(normal) / distance);
		float intensity = Math.min(1f / cos, light.getRadius() / (distance * distance));
		return light.getColor().getMul((cos * intensity) / 255f);
	}
}