package soya.framework.settler.evaluators.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import soya.framework.settler.*;
import soya.framework.settler.evaluators.AbstractEvaluator;
import soya.framework.settler.evaluators.AbstractEvaluatorBuilder;

@Component(name="json_path", arguments = "jsonPath")
public class JsonPathBuilder extends AbstractEvaluatorBuilder<JsonPathBuilder.JsonPathEvaluator> {

    @Override
    public JsonPathEvaluator build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        if(arguments.length != 1) {
            throw new IllegalFunctionArgumentException();
        }

        JsonPathEvaluator evaluator = new JsonPathEvaluator();
        AssignmentNode parameter = (AssignmentNode) arguments[0];
        evaluator.jsonPath = parameter.getStringValue(session.getContext());

        return evaluator;
    }

    static final class JsonPathEvaluator extends AbstractEvaluator {

        private String jsonPath;
        private JsonPathEvaluator() {
        }

        @Override
        public String evaluate(String data) throws EvaluateException {
            Configuration JACKSON_JSON_NODE_CONFIGURATION = Configuration.builder().jsonProvider(new GsonJsonProvider())
                    .options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();

            Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider())
                    .options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();

            return JsonPath.using(conf).parse(data).read(jsonPath).toString();
        }
    }
}
