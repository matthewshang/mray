package matl;

import java.util.ArrayList;

import core.Renderer;
import light.Light;
import math.Ray;
import math.Vector3f;
import prim.EngineObject;

public class PhongMaterial implements Material
{
	private Vector3f m_components;
	private int m_specularPower;
	private Vector3f m_color;

	public PhongMaterial(Vector3f components, int specularPower, Vector3f color)
	{
		m_components = components;
		m_specularPower = specularPower;
		m_color = color;
	}

	public Vector3f shadePoint(Renderer renderer, int lightSamples, Vector3f rayDirection, int depth, Vector3f point, Vector3f eye, Vector3f normal, ArrayList<Light> lights)
	{
		Vector3f totalDiffuse = Vector3f.zero();
		Vector3f totalSpecular = Vector3f.zero();

		for (int i = 0; i < lights.size(); i++)
		{
			Vector3f[] components = sampleLight(renderer, lightSamples, lights.get(i), point, normal, eye);
			totalDiffuse.add(components[0]);
			totalSpecular.add(components[1]);
		}

		Vector3f ambient = getAmbient(m_color).mul(m_components.getX());
		ambient.add(totalSpecular.mul(m_components.getZ()));
		totalDiffuse.mul(m_components.getY());

		return new Vector3f(Math.min(255f, m_color.getX() * totalDiffuse.getX() + ambient.getX()),
							Math.min(255f, m_color.getY() * totalDiffuse.getY() + ambient.getY()),
							Math.min(255f, m_color.getZ() * totalDiffuse.getZ() + ambient.getZ()));
	}

	private Vector3f[] sampleLight(Renderer renderer, int lightSamples, Light light, Vector3f point, Vector3f normal, Vector3f eye)
	{
		if (light.canBeSampled())
		{
			Vector3f diffuse = Vector3f.zero();
			Vector3f specular = Vector3f.zero();

			for (int i = 0; i < lightSamples; i++)
			{
				Vector3f sample = light.getPointOn(point);
				Ray lightRay = new Ray(point, sample.getSub(point));

				if (!renderer.isPointShadowed(lightRay))
				{
					diffuse.add(getDiffuse(sample, light.getColor(), light.getPower(), normal, point));
					specular.add(getSpecular(sample, light.getColor(), light.getPower(), normal, point, eye));
				}
			}

			diffuse.mul(1f / (float) lightSamples);
			specular.mul(1f/ (float) lightSamples);

			return new Vector3f[]{diffuse, specular};
		}
		else
		{
			if (!renderer.isPointShadowed(new Ray(point, light.getPointOn(point).getSub(point))))
			{
				return new Vector3f[]{getDiffuse(light.getPosition(), light.getColor(), light.getPower(), normal, point), 
									  getSpecular(light.getPosition(), light.getColor(), light.getPower(), normal, point, eye)};
			}
			else
			{
				return new Vector3f[]{Vector3f.zero(), Vector3f.zero()};
			}
		}
	}

	private Vector3f getAmbient(Vector3f color)
	{
		return color.getMul(m_components.getX());
	}

	private Vector3f getDiffuse(Vector3f lightPosition, Vector3f lightColor, float lightPower, Vector3f normal, Vector3f point)
	{
		Vector3f pointToLight = lightPosition.getSub(point);
		float distance = pointToLight.length();
		float cos = Math.max(0f, pointToLight.dot(normal) / distance);
		float intensity = Math.min(1f / cos, lightPower / (distance * distance));
		return lightColor.getMul((cos * intensity) / 255f);
	}

	private Vector3f getSpecular(Vector3f lightPosition, Vector3f lightColor, float lightPower, Vector3f normal, Vector3f point, Vector3f eye)
	{
		Vector3f pointToLight = lightPosition.getSub(point);
		Vector3f pointToEye = eye.getSub(point);
		Vector3f reflected = normal.reflect(pointToLight);
		float cos = Math.max(0f, pointToEye.dot(reflected) / (pointToEye.length() * reflected.length()));
		float distance = pointToLight.length();
		return lightColor.getMul((float) Math.pow(cos, m_specularPower)).mul(lightPower / (distance * distance));
	}
}