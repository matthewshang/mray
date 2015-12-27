package matl;

import java.util.ArrayList;

import core.Renderer;
import math.Ray;
import math.Vector3f;
import prim.EngineObject;
import prim.Light;

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

	public Vector3f shadePoint(Renderer renderer, Vector3f rayDirection, int depth, Vector3f point, Vector3f eye, Vector3f normal, ArrayList<Light> lights)
	{
		Vector3f totalDiffuse = Vector3f.zero();
		Vector3f totalSpecular = Vector3f.zero();

		for (int i = 0; i < lights.size(); i++)
		{
			Light light = lights.get(i);
			Ray lightRay = new Ray(point, light.getPosition().getSub(point));

			if (!renderer.isPointShadowed(lightRay))
			{
				totalDiffuse.add(getDiffuse(light, normal, point));
				totalSpecular.add(getSpecular(light, normal, point, eye));
			}
		}

		Vector3f ambient = getAmbient(m_color).mul(m_components.getX());
		ambient.add(totalSpecular.mul(m_components.getZ()));
		totalDiffuse.mul(m_components.getY());

		return new Vector3f(Math.min(255f, m_color.getX() * totalDiffuse.getX() + ambient.getX()),
							Math.min(255f, m_color.getY() * totalDiffuse.getY() + ambient.getY()),
							Math.min(255f, m_color.getZ() * totalDiffuse.getZ() + ambient.getZ()));
	}

	private Vector3f getAmbient(Vector3f color)
	{
		return color.getMul(m_components.getX());
	}

	private Vector3f getDiffuse(Light light, Vector3f normal, Vector3f point)
	{
		Vector3f pointToLight = light.getPosition().getSub(point);
		float distance = pointToLight.length();
		float cos = Math.max(0f, pointToLight.dot(normal) / distance);
		float intensity = Math.min(1f / cos, light.getRadius() / (distance * distance));
		return light.getColor().getMul((cos * intensity) / 255f);
	}

	private Vector3f getSpecular(Light light, Vector3f normal, Vector3f point, Vector3f eye)
	{
		Vector3f lightPosition = light.getPosition();
		Vector3f pointToLight = lightPosition.getSub(point);
		Vector3f pointToEye = eye.getSub(point);
		Vector3f reflected = normal.reflect(pointToLight);
		float cos = Math.max(0f, pointToEye.dot(reflected) / (pointToEye.length() * reflected.length()));
		float distance = pointToLight.length();
		return light.getColor().getMul((float) Math.pow(cos, m_specularPower)).mul(light.getRadius() / (distance * distance));
	}
}