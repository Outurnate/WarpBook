package panicnot42.warpbook;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils
{
  public static int round(double value, RoundingMode mode)
  {
    return new BigDecimal(value).setScale(0, mode).intValue();
  }
}
