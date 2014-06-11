package panicnot42.util;

import java.util.EventListener;

public interface UpdateTableListener extends EventListener
{
  public void tableUpdated(UpdateTableEvent updateTableEvent);
}
