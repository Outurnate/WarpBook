Warp Book
=========

This niche mod allows for easy teleportation between given points without admin privileges. It offers a simple alternative to the standard /home and /warp commands.

This is a continuation of the work done by [Panicnot42](https://github.com/Outurnate) in 2016.  This version features a multitude of bug fixes, graphic overhauls, and improved features.


What's changed from the original 1.8.9 version and why
------------------------------------------------------


Recipes and Items
-----------------

What Changed: Warp Potions were added.  
Rationale: Warp potions are a consumable warp item and a sensible crafting ingredient.  It's also a fluid so it can be used as a convenient fuel in the future.

What Changed: Warp clusters were added.  
Rationale: This makes the mod a bit less OP since more difficult to obtain resources are needed.  Clusters become the main ingredient for crafting to reduce recipe collision with other mods.

What Changed: Deathly warp page recipe has been changed back to the original 1.7.10 recipe.  
Rationale: This is a powerful feature that should require more expensive ingredients.

What Changed: Exploding potato pages were removed.  
Rationale: This appears to be a scope creep issue and a useless item that doesn't belong in the mod.

What Changed: Warp fuel has been removed.  
Rationale: This wasn't a fully formed concept and will be revisited using a better mechanism at a later date.


Warp Pages
----------

What Changed: Warp pages are no longer consumed on use.  
Rationale: Why would a page be consumed when used if it's not consumed in the book?  Warp potions replace the concept of the consumable warp item.

What Changed: Warp pages can no longer be erased to create an unbound warp page.  
Rationale: Warp pages should be permanant objects that cannot be undone, like a Minecraft map.

What Changed: Warp pages are crafted from paper and warp potion.  
Rationale: Warp potions make more sense for crafting because paper can soak up the warp potion whereas how a pearl becomes fused with paper is unclear.

What Changed: All warp pages have been graphically updated to contain a symbol when bound to a destination.  
Rationale: The graphics for all bound pages where blank with only a solid color.  It didn't look like anything was on them which was counter-intuitive. This also looks better.

What Changed: The player bound warp page has been recolored to red.  
Rationale: This was an aesthetic choice.  The purple page was ugly.


The Warp Book
-------------

What Changed: Warp book now accepts blank pages.  
Rationale: This provides a convenient place to store blank pages.

What Changed: The color of the warpbook is now natural leather with an ender pearl on the cover.  
Rationale: A stylistic choice to make the book feel more in line with the materials it's made of. Besides, there's no reason why the book would be blue. The ender pearl on the cover gives the player a hint at what it's used for.

What Changed: Warp book gui improvements.  Button graphics have been fixed. Fwd and Back buttons match vanilla book style. Gui color matches item color of warp book.  
Rationale: The gui did not match the Vanilla Minecraft book enough for consistancy. The gui looked broken.

What Changed: Warp book can now be dyed to the 16 Minecraft colors.
Rationale: It's awesome.


The Book Cloner
---------------

What Changed: The book cloner block recipe has been redone.  
Rationale: The old recipe didn't make any sense and the finished product looked nothing like it's components.

What Changed: The book cloner block model has been redesigned.  
Rationale: The old book cloner didn't look like a completed design. The new design looks more like a workbench and has ender pearl accents to imply it's purpose.

What Changed: The book cloner no longer produces warp plates and instead outputs whole complete books.  
Rationale: If the cloner is made to accept blank warp pages and an empty book then it doesn't need a warp plate item.

What Changed: Warp plate item removed.  
Rationale: No longer neccessary if the cloner doesn't use it.

What Changed: The book cloner gui has been redesigned. It has graphical hints for how to use it.  
Rationale: The improved workflow for cloning a book demanded a gui overhaul. 



The Teleporter
--------------

What Changed: The teleporter now accepts a warp potion as it's warp location data.  
Rationale: This makes the teleporter more simple. The potion is consumed. It doesn't return a page when clicked on by accident. The location can only be overwritten with another potion.

What Changed: The teleporter is crafted from end rods requiring a late game construction.  
Rationale: This gives the player something to work for so powerful technology isn't just available with little effort.

What Changed: The teleporter's appearance has been redesigned.  
Rationale: The teleporter looked like a strange pressure plate that wasn't textured properly.


Console Commands
----------------

What Changed: Using /waypointlist in the console will also show coordinates, dimension, and extra info for each waypoint.  
Rationale: Listing only the name was not informative enough.
