package com.panicnot42.warpbook;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.panicnot42.warpbook.util.asm.PassThroughLabelMap;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.world.World;

public class ImprobabilitityDriveClassTransformer implements IClassTransformer
{
  @Override
  public byte[] transform(String name, String transformedName, byte[] basicClass)
  {
    if (name.equals("ajk")) // BlockFarmland
    {
      return patchClassASMFarmland(name, basicClass, true);
    }
    if (name.equals("net.minecraft.block.BlockFarmland"))
    {
      return patchClassASMFarmland(name, basicClass, false);
    }
    if (name.equals("afn")) // World
    {
      return patchClassASMWorld(name, basicClass, true);
    }
    if (name.equals("net.minecraft.world.World")) // World
    {
      return patchClassASMWorld(name, basicClass, false);
    }
    return basicClass;
  }
  
  private byte[] patchClassASMWorld(String name, byte[] basicClass, boolean obfuscated)
  {
    String targetMethodName = obfuscated ? "c" : "getCelestialAngle"; // (F)F
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(basicClass);
    classReader.accept(classNode, 0);
    
    Iterator<MethodNode> methods = classNode.methods.iterator();
    while(methods.hasNext())
    {
      AbstractInsnNode floadnode = null;
      
      MethodNode m = methods.next();
      if (m.name.equals(targetMethodName) && m.desc.equals("(F)F"))
      {
        Iterator<AbstractInsnNode> iter = m.instructions.iterator();
        
        while(iter.hasNext())
        {
          floadnode = iter.next();
          
          if (floadnode.getOpcode() == 0x17) // FLOAD
            break;
        }
        InsnList inject = new InsnList();
        CoreUtils.getDaylightSpeed();
        inject.add(new MethodInsnNode(0xB8, "com/panicnot42/CoreUtils", "getDaylightSpeed", "()F")); // INVOKESTATIC
        inject.add(new InsnNode(0x6A)); // FMUL
        
        m.instructions.insert(floadnode, inject);
        break;
      }
    }
    
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    classNode.accept(writer);
    return writer.toByteArray();
  }

  private byte[] patchClassASMFarmland(String name, byte[] basicClass, boolean obfuscated)
  {
    String targetMethodName = obfuscated ? "a" : "onFallenUpon"; // (Lafn;IIILqn;F)V
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(basicClass);
    classReader.accept(classNode, 0);
    
    Iterator<MethodNode> methods = classNode.methods.iterator();
    while(methods.hasNext())
    {
      MethodNode m = methods.next();
      if (m.name.equals(targetMethodName) && m.desc.equals(obfuscated ? "(Lafn;IIILqn;F)V" : "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V"))
      {
        AbstractInsnNode ifnenode = null;
        
        Iterator<AbstractInsnNode> iter = m.instructions.iterator();
        while(iter.hasNext())
        {
          ifnenode = iter.next();
          if (ifnenode.getOpcode() == 0x9A) // IFNE
            break;
        }
        InsnList inject = new InsnList();

        inject.add(new VarInsnNode(0x19, 5)); // ALOAD
        inject.add(new MethodInsnNode(0xB8, "com/panicnot42/CoreUtils", "canTriggerWalking", obfuscated ? "(Lqn;)Z" : "(Lnet/minecraft/entity/Entity;)Z")); // INVOKESTATIC
        JumpInsnNode ifeqnode = (JumpInsnNode)ifnenode.clone(new PassThroughLabelMap());
        ifeqnode.setOpcode(0x99); //IFEQ
        inject.add(ifeqnode); //IFEQ L1
        
        m.instructions.insert(ifnenode, inject);
        break;
      }
    }
    
    //after first IFNE
    //ALOAD 5
    //INVOKESTATIC com/panicnot42/CoreUtils.canTriggerWalking (Lnet/minecraft/entity/Entity;)Z
    //IFEQ L1
    
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    classNode.accept(writer);
    return writer.toByteArray();
  }
}
