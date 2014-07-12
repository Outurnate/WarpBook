package com.panicnot42.warpbook.util.net;

import java.util.EventListener;

public interface UpdateTableListener extends EventListener
{
  public void tableUpdated(UpdateTableEvent updateTableEvent);
}
