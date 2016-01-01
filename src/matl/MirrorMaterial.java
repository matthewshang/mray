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
	private float m_glossiness;
	private float m_reflectedAmount;
	private float m_diffuseAmount;

	public MirrorMaterial(Vector3f color, Vector3f tint, float glossiness, float reflectedAmount, float diffuseAmount)
	{
		m_color = color;
		m_tint = tint;
		m_glossiness = glossiness;
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
				totalDiffuse.add(sampleLight(renderer, lights.get(i), point, normal, eye));
			}
		}

		Vector3f reflectedDirection = normal.reflect(rayDirection).mul(-1f).normalize();
		Vector3f reflectedColor = Vector3f.zero();

		if (m_glossiness == 0f)
		{
			reflectedColor.add(renderer.traceRay(new Ray(point, reflectedDirection), depth + 1));
		}
		else
		{
			for (int i = 0; i < renderer.getReflectionSamples(); i++)
			{
				reflectedColor.add(renderer.traceRay(new Ray(point, Sampler.sampleSphere(reflectedDirection, 1f, 0f, (float) Math.PI * m_glossiness / 2f)), depth + 1));

			}

			reflectedColor.mul(1f / (float) renderer.getReflectionSamples());
		}

		return reflectedColor.getMulComponents(m_tint).mul(m_reflectedAmount).add(m_color.getMulComponents(totalDiffuse.mul(m_diffuseAmount)));
	}

	private Vector3f sampleLight(Renderer renderer, Light light, Vector3f point, Vector3f normal, Vector3f eye)
	{
		if (light.canBeSampled())
		{
			Vector3f diffuse = Vector3f.zero();

			for (int i = 0; i < renderer.getLightSamples(); i++)
			{
				Vector3f sample = light.getPointOn(point);
				Ray lightRay = new Ray(point, sample.getSub(point));

				if (!renderer.isPointShadowed(lightRay))
				{
					diffuse.add(getDiffuse(sample, light.getColor(), light.getPower(), normal, point));
				}
			}

			diffuse.mul(1f / (float) renderer.getLightSamples());

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