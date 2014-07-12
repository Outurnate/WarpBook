package com.panicnot42.warpbook.util.asm;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.tree.LabelNode;

// I hate that this class has to exist... :(
public class PassThroughLabelMap implements Map<LabelNode, LabelNode>
{
  @Override
  public void clear()
  {
  }

  @Override
  public boolean containsKey(Object arg0)
  {
    return false;
  }

  @Override
  public boolean containsValue(Object arg0)
  {
    return false;
  }

  @Override
  public Set<java.util.Map.Entry<LabelNode, LabelNode>> entrySet()
  {
    return null;
  }

  @Override
  public LabelNode get(Object arg0)
  {
    return (LabelNode)arg0;
  }

  @Override
  public boolean isEmpty()
  {
    return false;
  }

  @Override
  public Set<LabelNode> keySet()
  {
    return null;
  }

  @Override
  public LabelNode put(LabelNode arg0, LabelNode arg1)
  {
    return arg0;
  }

  @Override
  public void putAll(Map<? extends LabelNode, ? extends LabelNode> arg0)
  {
  }

  @Override
  public LabelNode remove(Object arg0)
  {
    return (LabelNode)arg0;
  }

  @Override
  public int size()
  {
    return 0;
  }

  @Override
  public Collection<LabelNode> values()
  {
    return null;
  }
}
