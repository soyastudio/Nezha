package soya.framework.settler.evaluators.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import soya.framework.settler.*;
import soya.framework.settler.evaluators.AbstractEvaluatorBuilder;

@Component(name = "json_foreach")
public final class JsonForEachBuilder implements EvaluatorBuilder<JsonForEachBuilder.JsonForEach> {

    @Override
    public JsonForEach build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        JsonForEach eval = new JsonForEach();
        return eval;
    }

    static class JsonForEach extends JsonElementEvaluator {

        private JsonElementEvaluator evaluator;
        private JsonForEach() {
        }

        @Override
        public JsonElement evaluate(JsonElement jsonElement) throws EvaluateException {
            JsonArray result = new JsonArray();
            JsonArray array = jsonElement.getAsJsonArray();
            array.forEach(e -> {
                result.add(evaluator.evaluate(e));
            });

            return result;
        }
    }
}
