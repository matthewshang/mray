package prim;

import math.Vector3f;

public class Material
{
	public final static int COMPONENT_AMBIENT = 0;
	public final static int COMPONENT_DIFFUSE = 1;
	public final static int COMPONENT_SPECULAR = 2;

	private Vector3f m_components;
	private int m_specularPower;
	private Vector3f m_color;
	private boolean m_isReflective;

	public Material(Vector3f components, int specularPower, Vector3f color, boolean isReflective)
	{
		m_components = components;
		m_specularPower = specularPower;
		m_color = color;
		m_isReflective = isReflective;
	}

	public float getComponent(int component)
	{
		switch (component)
		{
			case COMPONENT_AMBIENT:
				return m_components.getX();

			case COMPONENT_DIFFUSE:
				return m_components.getY();

			case COMPONENT_SPECULAR:
				return m_components.getZ();

			default:
				return 0f;
		}
	}

	public int getSpecularPower()
	{
		return m_specularPower;
	}

	public Vector3f getColor()
	{
		return m_color;
	}

	public boolean isReflective()
	{
		return m_isReflective;
	}
}