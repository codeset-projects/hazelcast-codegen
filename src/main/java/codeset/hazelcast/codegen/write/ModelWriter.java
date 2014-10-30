package codeset.hazelcast.codegen.write;

import java.io.File;
import java.io.IOException;

import codeset.hazelcast.codegen.CodegenException;

import com.sun.codemodel.JCodeModel;

public class ModelWriter {

    public void write(String target, JCodeModel codeModel) {

        File file = new File(target);
        if(!file.exists()) {
            file.mkdirs();
        }

        try {
            codeModel.build(file);
        } catch (IOException e) {
            throw new CodegenException("Failed to write classes:", e);
        }

    }
}
