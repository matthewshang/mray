package prim;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import math.AABB;
import math.Vector3f;

public class OBJLoader
{
	public static GeometryData loadFromFile(String filePath)
	{
		GeometryData geometry = new GeometryData();

		FileInputStream fileIn = null;
		BufferedReader reader = null;

		try
		{
			fileIn = new FileInputStream(filePath);
			reader = new BufferedReader(new InputStreamReader(fileIn));
			String line;

			Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
			Vector3f max = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);

			do
			{
				line = reader.readLine();

				if (line != null && !line.trim().equals("") && !line.substring(0, 1).equals("#"))
				{
					switch (line.substring(0, 2).trim())
					{
						case "v":
							parseVertex(line, geometry, min, max);
							break;

						case "f":
							parseTriangle(line, geometry);
							break;

						default:
							break;
					}
				}
			}
			while (line != null);

			geometry.setBoundingBox(new AABB(min, max));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			System.out.println("Could not load model at " + filePath);
		}
		finally
		{
			try
			{
				if (reader != null)
				{
					reader.close();
				}

				if (fileIn != null)
				{
					fileIn.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			} 
		}

		return geometry;
	}

	private static void parseVertex(String line, GeometryData geometry, Vector3f min, Vector3f max)
	{
		String parsed = line.substring(1);
		String[] vertexData = parsed.trim().split("\\s");
		float x = Float.parseFloat(vertexData[0]);
		float y = Float.parseFloat(vertexData[1]);
		float z = Float.parseFloat(vertexData[2]);

		if (x < min.getX())
		{
			min.setX(x);
		}
		else if (x > max.getX())
		{
			max.setX(x);
		}

		if (y < min.getY())
		{
			min.setY(y);
		}
		else if (y > max.getY())
		{
			max.setY(y);
		}

		if (z < min.getZ())
		{
			min.setZ(z);
		}
		else if (z > max.getZ())
		{
			max.setZ(z);
		}

		geometry.addVertex(new Vector3f(x, y, z));
	}

	private static void parseTriangle(String line, GeometryData geometry)
	{
		String parsed = line.substring(1);
		String[] triangleData = parsed.trim().split("\\s");
		geometry.addTriangle(Integer.parseInt(triangleData[0].split("/")[0]) - 1,
							 Integer.parseInt(triangleData[1].split("/")[0]) - 1,
							 Integer.parseInt(triangleData[2].split("/")[0]) - 1);
	}
}