package matl;

import java.util.ArrayList;

import core.Renderer;
import light.Light;
import math.Vector3f;
import math.Ray;

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
				totalDiffuse.add(sampleLight(renderer, lights.get(i), point, normal, eye)[0]);
			}
		}

		Vector3f reflected = normal.reflect(rayDirection).mul(-1f);
		Vector3f reflectedColor = renderer.traceRay(new Ray(reflected.getMul(0.001f).add(point), reflected), depth + 1);
		return reflectedColor.getMulComponents(m_tint).mul(m_reflectedAmount).add(m_color.getMulComponents(totalDiffuse.mul(m_diffuseAmount)));
	}

	public Vector3f[] sampleLight(Renderer renderer, Light light, Vector3f point, Vector3f normal, Vector3f eye)
	{
		if (light.canBeSampled())
		{
			Vector3f diffuse = Vector3f.zero();

			for (int i = 0; i < 256; i++)
			{
				Vector3f sample = light.getPointOn(point);
				Ray lightRay = new Ray(point, sample.getSub(point));

				if (!renderer.isPointShadowed(lightRay))
				{
					diffuse.add(getDiffuse(sample, light.getColor(), light.getPower(), normal, point));
				}
			}

			diffuse.mul(1f / 256f);

			return new Vector3f[]{diffuse};
		}
		else
		{
			if (!renderer.isPointShadowed(new Ray(point, light.getPointOn(point).getSub(point))))
			{
				return new Vector3f[]{getDiffuse(light.getPosition(), light.getColor(), light.getPower(), normal, point)};
			}
			else
			{
				return new Vector3f[]{Vector3f.zero()};
			}
		}

	}

	private Vector3f getDiffuse(Vector3f lightPosition, Vector3f lightColor, float lightPower, Vector3f normal, Vector3f point)
	{
		Vector3f pointToLight = lightPosition.getSub(point);
		float distance = pointToLight.length();
		float cos = Math.max(0f, pointToLight.dot(normal) / distance);
		float intensity = Math.min(1f / cos, lightPower / (distance * distance));
		return lightColor.getMul((cos * intensity) / 255f);
	}
}