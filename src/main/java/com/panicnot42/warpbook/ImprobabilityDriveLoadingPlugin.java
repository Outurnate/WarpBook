package com.panicnot42.warpbook;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class ImprobabilityDriveLoadingPlugin implements IFMLLoadingPlugin
{
  @Override
  public String[] getASMTransformerClass()
  {
    return new String[]
        {
          ImprobabilitityDriveClassTransformer.class.getName()
        };
  }

  @Override
  public String getModContainerClass()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getSetupClass()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void injectData(Map<String, Object> data)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public String getAccessTransformerClass()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
