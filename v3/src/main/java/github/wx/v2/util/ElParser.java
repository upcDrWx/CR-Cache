package github.wx.v3.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.TreeMap;

/**
 * @author wx
 * @description
 * @date 2023/11/14 15:36
 */
public class ElParser {

    public static String parse(String elString, TreeMap<String, Object> map) {
        elString = String.format("#{%s}", elString);

        // 创建表达式解析器
        ExpressionParser parser = new SpelExpressionParser();
        // 通过 evaluationContext.setVariable 可以在上下文中设定变量
        EvaluationContext context = new StandardEvaluationContext();

        map.entrySet().forEach(entry -> context.setVariable(entry.getKey(), entry.getValue()));

        // 解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文
        Expression expression = parser.parseExpression(elString, new TemplateParserContext());
        // 使用 Expression.getValue() 获取表达式的值，这里传入了 Evaluation 上下文
        String value = expression.getValue(context, String.class);
        return value;

    }
}
