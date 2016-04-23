package API;

import spark.ResponseTransformer;

/**
 * Created by vishalkuo on 2016-04-23.
 */
public class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) throws Exception {
        return model.toString();

    }
}
