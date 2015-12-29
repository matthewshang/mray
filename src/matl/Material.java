package matl;

import java.util.ArrayList;

import core.Renderer;
import light.Light;
import math.Vector3f;
import prim.EngineObject;

public interface Material
{
	public Vector3f shadePoint(Renderer renderer, Vector3f rayDirection, int depth, Vector3f point, Vector3f eye, Vector3f normal, ArrayList<Light> lights);
	public Vector3f[] sampleLight(Renderer renderer, Light light, Vector3f point, Vector3f normal, Vector3f eye);
}