package com.panicnot42.warpbook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.panicnot42.warpbook.util.ITrampleHandler;

import net.minecraft.entity.Entity;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;

public class CoreUtils
{
  private static float daylightSpeed = 1.0f;
  private static ArrayList<ITrampleHandler> trampleHandlers = new ArrayList<ITrampleHandler>();
  
  public static boolean canTriggerWalking(Entity entity)
  {
    boolean shouldTrample = true;
    try
    {
      Method canTriggerWalking = entity.getClass().getDeclaredMethod("canTriggerWalking");
      canTriggerWalking.setAccessible(true);
      shouldTrample = (Boolean)canTriggerWalking.invoke(entity);
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchMethodException e)
    {
      e.printStackTrace();
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
    for(ITrampleHandler trampleHandler : trampleHandlers)
      shouldTrample = shouldTrample ? trampleHandler.shouldTrample(entity) : shouldTrample;
    return shouldTrample;
  }
  
  public static void setDaylightSpeed(float multiplier)
  {
    daylightSpeed = multiplier;
  }
  
  public static float getDaylightSpeed()
  {
    return daylightSpeed;
  }
  
  public static void addTrampleHandler(ITrampleHandler trampleHandler)
  {
    trampleHandlers.add(trampleHandler);
  }
  
  public static void removeTrampleHandler(ITrampleHandler trampleHandler)
  {
    trampleHandlers.remove(trampleHandler);
  }

  public final WorldProvider provider = null;
  private WorldInfo worldInfo = null;
  public float getCelestialAngle(float par1)
  {
      return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), par1 * getDaylightSpeed());
  }
}
