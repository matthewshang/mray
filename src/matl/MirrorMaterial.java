package matl;

import java.util.ArrayList;

import core.Renderer;
import light.Light;
import math.Vector3f;
import math.Ray;
import math.Sampler;

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

	public Vector3f shadePoint(Renderer renderer, int lightSamples, Vector3f rayDirection, int depth, Vector3f point, Vector3f eye, Vector3f normal, ArrayList<Light> lights)
	{
		Vector3f totalDiffuse = Vector3f.zero();

		if (m_diffuseAmount > 0f)
		{
			for (int i = 0; i < lights.size(); i++)
			{
				totalDiffuse.add(sampleLight(renderer, lightSamples, lights.get(i), point, normal, eye));
			}
		}

		Vector3f reflected = normal.reflect(rayDirection).mul(-1f).normalize();
		Vector3f total = Vector3f.zero();
		for (int i = 0; i < 16; i++)
		{
			total.add(renderer.traceRay(new Ray(point, Sampler.sampleSphere(reflected, 1f, 0f, (float) Math.PI / 12f)), depth + 1));

		}
		Vector3f reflectedColor = total.mul(1f / 16f);
		return reflectedColor.getMulComponents(m_tint).mul(m_reflectedAmount).add(m_color.getMulComponents(totalDiffuse.mul(m_diffuseAmount)));
	}

	private Vector3f sampleLight(Renderer renderer, int lightSamples, Light light, Vector3f point, Vector3f normal, Vector3f eye)
	{
		if (light.canBeSampled())
		{
			Vector3f diffuse = Vector3f.zero();

			for (int i = 0; i < lightSamples; i++)
			{
				Vector3f sample = light.getPointOn(point);
				Ray lightRay = new Ray(point, sample.getSub(point));

				if (!renderer.isPointShadowed(lightRay))
				{
					diffuse.add(getDiffuse(sample, light.getColor(), light.getPower(), normal, point));
				}
			}

			diffuse.mul(1f / (float) lightSamples);

			return diffuse;
		}
		else
		{
			if (!renderer.isPointShadowed(new Ray(point, light.getPointOn(point).getSub(point))))
			{
				return getDiffuse(light.getPosition(), light.getColor(), light.getPower(), normal, point);
			}
			else
			{
				return Vector3f.zero();
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