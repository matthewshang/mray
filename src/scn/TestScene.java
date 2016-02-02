package scn;

import light.PointLight;
import light.SphereLight;
import math.Quaternion;
import math.Vector3f;
import matl.DiffuseMaterial;
import matl.DiffuseMaterial;
import matl.MirrorMaterial;
import prim.Cylinder;
import prim.GeometryInstance;
import prim.OBJLoader;
import prim.Plane;
import prim.Sphere;
import prim.GeometryData;

// Temperory - Will put in scene describing text file parsing in later
public class TestScene
{
	public static Scene head()
	{
		Scene scene = new Scene();
		DiffuseMaterial white = new DiffuseMaterial(new Vector3f(255f, 255f, 255f));

		GeometryData head = OBJLoader.loadFromFile("./res/head.OBJ");
		Quaternion r1 = new Quaternion();
		r1.setFromAxisAngle(new Vector3f(0f, 1f, 0f), ((float) Math.PI * 180f) / 180f);
		scene.addObject(new GeometryInstance(new Vector3f(0f, 2f, 12f), new Vector3f(30f, 30f, 30f), r1, head, white));
		scene.addLight(new SphereLight(new Vector3f(-20f, 20f, 0f), 500f, 2f, new Vector3f(182f, 126f, 91f)));

		return scene;
	}

	public static Scene box()
	{
		Scene scene = new Scene();
		DiffuseMaterial white = new DiffuseMaterial(new Vector3f(255f, 255f, 255f));
		DiffuseMaterial red = new DiffuseMaterial(new Vector3f(255f, 0f, 0f));
		DiffuseMaterial blue = new DiffuseMaterial(new Vector3f(0f, 0f, 255f));
		MirrorMaterial mirror = new MirrorMaterial(new Vector3f(255f, 255f, 255f), 0f, 0.8f);


		GeometryData cube = OBJLoader.loadFromFile("./res/cube.obj");

		Quaternion r1 = new Quaternion();
		r1.setFromAxisAngle(new Vector3f(0f, 1f, 0f), ((float) Math.PI * -30f) / 180f);
		scene.addObject(new GeometryInstance(new Vector3f(-3f, -2f, 12f), new Vector3f(4f, 4f, 4f), r1, cube, white));

		Quaternion r2 = new Quaternion();
		r2.setFromAxisAngle(new Vector3f(0f, 1f, 0f), ((float) Math.PI * 30f) / 180f);
		scene.addObject(new GeometryInstance(new Vector3f(2f, -3f, 8f), new Vector3f(2f, 2f, 2f), r2, cube, white));

		scene.addObject(new Sphere(new Vector3f(-0.5f, -3, 9f), 1f, mirror));

		scene.addObject(new GeometryInstance(new Vector3f(0f, -9f, 10f), new Vector3f(10f, 10f, 10f), new Quaternion(), cube, white));
		// scene.addObject(new GeometryInstance(new Vector3f(0f, 11f, 10f), new Vector3f(10f, 10f, 10f), new Quaternion(), cube, white));
		scene.addObject(new GeometryInstance(new Vector3f(0f, 15f, 10f), new Vector3f(10f, 10f, 10f), new Quaternion(), cube, white));

		scene.addObject(new GeometryInstance(new Vector3f(0f, 1f, 20f), new Vector3f(10f, 10f, 10f), new Quaternion(), cube, white));

		scene.addObject(new GeometryInstance(new Vector3f(10f, 1f, 10f), new Vector3f(10f, 10f, 10f), new Quaternion(), cube, red));
		scene.addObject(new GeometryInstance(new Vector3f(-10f, 1f, 10f), new Vector3f(10f, 10f, 10f), new Quaternion(), cube, blue));

		scene.addLight(new SphereLight(new Vector3f(0f, 5f, 10f), 5f, 0.5f, new Vector3f(255f, 255f, 255f)));

		return scene;
	}

	public static Scene cube()
	{
		Scene scene = new Scene();

		GeometryData ico = OBJLoader.loadFromFile("./res/icosahedron.obj");
		GeometryData cube = OBJLoader.loadFromFile("./res/cube.obj");
		GeometryData monkey = OBJLoader.loadFromFile("./res/monkey.obj");
		GeometryData teapot = OBJLoader.loadFromFile("./res/teapot.obj");

		DiffuseMaterial m1 = new DiffuseMaterial(new Vector3f(255f, 255f, 255f));
		MirrorMaterial m2 = new MirrorMaterial(new Vector3f(255f, 255f, 255f), 0f, 0.8f);
		MirrorMaterial m3 = new MirrorMaterial(new Vector3f(255f, 255f, 255f), 0.02f, 1f);

		// cube
		Quaternion r = new Quaternion();
		r.setFromAxisAngle(new Vector3f(0f, 1f, 0f), ((float) Math.PI * 30f) / 180f);
		scene.addObject(new GeometryInstance(new Vector3f(0f, -3f, 19f), new Vector3f(4f, 4f, 4f), r, cube, m2));
		
		// ico
		Quaternion r2 = new Quaternion();
		r2.setFromAxisAngle(new Vector3f(0f, 1f, 0f), ((float) Math.PI * 45f) / 180f);
		scene.addObject(new GeometryInstance(new Vector3f(0f, 0.7f, 19f), new Vector3f(2f, 2f, 2f), r2, ico, m1));

		// monkey
		Quaternion rot = new Quaternion();
		rot.setFromAxisAngle(new Vector3f(1f, 0f, 0f), (float) Math.PI / 2f);
		Quaternion rot2 = new Quaternion();
		rot2.setFromAxisAngle(new Vector3f(0f, 0f, 1f), (float) Math.PI);
		// scene.addObject(new GeometryInstance(new Vector3f(-5f, -3f, 15f), new Vector3f(2f, 2f, 2f), rot.multiply(rot2), monkey, m1));
		
		// teapot
		scene.addObject(new GeometryInstance(new Vector3f(5f, -5f, 15f), new Vector3f(0.05f, 0.05f, 0.05f), new Quaternion(), teapot, m1));

		scene.addObject(new Plane(new Vector3f(0f, -5f, 0f), new Vector3f(0f, 1f, 0f), m1));

		scene.addLight(new SphereLight(new Vector3f(-20f, 20f, 0f), 500f, 2f, new Vector3f(182f, 126f, 91f)));

		return scene;
	}

	public static Scene dragon()
	{
		Scene scene = new Scene();

		GeometryData dragon = OBJLoader.loadFromFile("./res/dragon.obj");

		DiffuseMaterial white = new DiffuseMaterial(new Vector3f(254f, 254f, 254f));
		MirrorMaterial mirror = new MirrorMaterial(new Vector3f(254f, 254f, 254f), 0f, 0.25f);
		MirrorMaterial gold = new MirrorMaterial(new Vector3f(255f, 150f, 0f), 0.1f, 0.45f);
		MirrorMaterial silver = new MirrorMaterial(new Vector3f(192f, 192f, 192f), 0.1f, 0.45f);

		scene.addObject(new Plane(new Vector3f(0f, -2f, 0f), new Vector3f(0f, 1f, 0f), mirror));

		Quaternion q1 = new Quaternion();
		q1.setFromAxisAngle(new Vector3f(0f, 1f, 0f), (float) Math.PI * -30f / 180f);

		scene.addObject(new GeometryInstance(new Vector3f(-4f, -2f, 10f), new Vector3f(0.5f, 0.5f, 0.5f), q1, dragon, gold));
		scene.addObject(new GeometryInstance(new Vector3f(4f, -2f, 10f), new Vector3f(0.5f, 0.5f, 0.5f), q1, dragon, silver));
		scene.addObject(new GeometryInstance(new Vector3f(0f, -2f, 10f), new Vector3f(0.5f, 0.5f, 0.5f), q1, dragon, white));

		scene.addLight(new SphereLight(new Vector3f(-20f, 20f, 0f), 500f, 2f, new Vector3f(182f, 126f, 91f)));
		return scene;
	}

	public static Scene bunny()
	{
		Scene scene = new Scene();

		GeometryData bunny = OBJLoader.loadFromFile("./res/bunny.obj");
		GeometryData cube = OBJLoader.loadFromFile("./res/cube.obj");

		MirrorMaterial mirror = new MirrorMaterial(new Vector3f(254f, 254f, 254f), 0f, 0.6f);
		MirrorMaterial mirror2 = new MirrorMaterial(new Vector3f(254f, 254f, 254f), 0.05f, 0.8f);
		DiffuseMaterial m2 = new DiffuseMaterial(new Vector3f(254f, 254f, 254f));
		DiffuseMaterial orange = new DiffuseMaterial(new Vector3f(254f, 128f, 0f));
		DiffuseMaterial purple = new DiffuseMaterial(new Vector3f(178f, 102f, 254f));


		scene.addObject(new Plane(new Vector3f(0f, -2f, 0f), new Vector3f(0f, 1f, 0f), m2));
		scene.addObject(new Sphere(new Vector3f(-3f, -0.5f, 12f), 1.5f, mirror));
		scene.addObject(new Sphere(new Vector3f(-3.5f, -1.25f, 9f), 0.75f, orange));
		scene.addObject(new Sphere(new Vector3f(4f, -1.5f, 9.5f), 0.5f, purple));


		Quaternion q1 = new Quaternion();
		q1.setFromAxisAngle(new Vector3f(0f, 1f, 0f), (float) Math.PI * -180f / 180f);
		scene.addObject(new GeometryInstance(new Vector3f(0f, -2.9f, 10f), new Vector3f(30f, 30f, 30f), q1, bunny, m2));

		Quaternion q2 = new Quaternion();
		q2.setFromAxisAngle(new Vector3f(0f, 1f, 0f), (float) Math.PI * -55f / 180f);
		scene.addObject(new GeometryInstance(new Vector3f(5.5f, 0f, 14f), new Vector3f(4f, 4f, 4f), q2, cube, mirror2));

		scene.addLight(new SphereLight(new Vector3f(-20f, 20f, 0f), 500f, 2f, new Vector3f(182f, 126f, 91f)));

		return scene;
	}
}