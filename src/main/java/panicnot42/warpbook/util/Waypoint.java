package panicnot42.warpbook.util;

public class Waypoint
{
  public String name;
  public int x, y, z, dim;

  public Waypoint(String name, int x, int y, int z, int dim)
  {
    this.name = name;
    this.x = x;
    this.y = y;
    this.z = z;
    this.dim = dim;
  }
}