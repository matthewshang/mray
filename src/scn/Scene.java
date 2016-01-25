package scn;

import java.util.ArrayList;

import light.Light;
import math.Vector3f;
import prim.EngineObject;

public class Scene
{
	private ArrayList<EngineObject> m_objects;
	private ArrayList<Light> m_lights;
	private static Vector3f m_skyColor = new Vector3f(204f, 229f, 255f);
	// private static Vector3f m_skyColor = new Vector3f(255f, 255f, 255f);


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

	public ArrayList<EngineObject> getObjects()
	{
		return m_objects;
	}

	public ArrayList<Light> getLights()
	{
		return m_lights;
	}

	public Vector3f getSkyColor()
	{
		return m_skyColor;
	}

}