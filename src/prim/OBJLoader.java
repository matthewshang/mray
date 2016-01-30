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
			Vector3f max = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);

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
			geometry.buildTree();
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
		Vector3f vertex = new Vector3f(Float.parseFloat(vertexData[0]),
									   Float.parseFloat(vertexData[1]),
									   Float.parseFloat(vertexData[2]));
		vertex.sortMinAndMax(min, max);
		geometry.addVertex(vertex);
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