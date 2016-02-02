package matl;

import java.util.ArrayList;

import core.Renderer;
import light.Light;
import math.Vector3f;
import prim.EngineObject;

public interface Material
{
	public Vector3f sample(Vector3f normal, Vector3f fromDirection, float random);
	public float getIndirectAmount(float random);
	public Vector3f getColor();
	public boolean receivesDirect(float random);
}