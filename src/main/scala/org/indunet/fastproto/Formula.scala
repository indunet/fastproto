package org.indunet.fastproto
import java.util.function

object Formula {
  val TEMPLATE =
    """
      | package org.indunet.fastproto.formula;
      |
      | import java.util.function.Function;
      | import org.indunet.fastproto.FormulaBuilder;
      |
      | public class %s implements FormulaBuilder {
      |   @Override
      |   public Function build() {
      |     Function<Integer, Object> func = %s
      |
      |     return func;
      |   }
      | }
      |""".stripMargin
}
