package thinking.cloud.generator.file.impl;

import java.io.PrintStream;
import java.util.List;

import thinking.cloud.generator.bean.TableClassMapping;
import thinking.cloud.generator.file.OutputJavaFile;
import thinking.cloud.generator.utils.PackageNameUtils;
import thinking.cloud.utils.reflect.ReflectBeanUtils;

/**
 * 输出controller 文件
 * @author zhouxinke
 * @date 2021年4月29日
 */
public class OutputController extends OutputJavaFile {

	public OutputController(String outputFilePath, List<TableClassMapping> tableClassMapping) {
		super(outputFilePath, tableClassMapping);
	}

	@Override
	public String getFileName(TableClassMapping table) {
		return table.getControllerName();
	}

	@Override
	protected void outputPackage(TableClassMapping table, PrintStream ps) {
		String packageName = PackageNameUtils.controllerPackage();
		ps.println("package "+packageName+";");
		
	}

	@Override
	protected void outputAnnotation(TableClassMapping table, PrintStream ps) {
		ps.println("@RestController");
		ps.println("@AllArgsConstructor");
	}

	@Override
	protected void outputClassName(TableClassMapping table, PrintStream ps) {
		ps.println("public class "+table.getControllerName()+" implements "+table.getApiName()+" {");
	}

	@Override
	protected void outputClassFiled(TableClassMapping table, PrintStream ps) {
		ps.println("private final "+table.getAdapterName()+ " " +ReflectBeanUtils.className2FieldName(null, table.getAdapterName())+" ;");
	}

	@Override
	protected void outputMethod(TableClassMapping table, PrintStream ps) {
		String adapterName = ReflectBeanUtils.className2FieldName(null, table.getAdapterName());
		ps.println("@Override");
		ps.println(methodIndent+"public Message<"+table.getVoName()+"> save(" +table.getBoName()+ " bo){" );
		ps.println(methodBodyIndent+table.getVoName() +" vo = "+adapterName+".add(bo);"); 
		ps.println(methodBodyIndent+"return Message.success(vo);");
		ps.println(methodIndent+"}");
		
		ps.println("@Override");
		ps.println(methodIndent+"public Message<"+table.getVoName()+"> delete(" +table.getPk().getFieldType()+ " id){" );
		ps.println(methodBodyIndent+adapterName+".delete(id);"); 
		ps.println(methodBodyIndent+"return Message.success();");
		ps.println(methodIndent+"}");
		
		
		ps.println("@Override");
		ps.println(methodIndent+"public Message<"+table.getVoName()+"> update(" +table.getPk().getFieldType()+ " id, "+table.getBoName()+ " bo){");
		ps.println(methodBodyIndent+"int count = "+adapterName+".update(id, bo);"); 
		ps.println(methodBodyIndent+"return  count==0?Message.failure(\"数据不存在\"):Message.success();");
		ps.println(methodIndent+"}");
		
		ps.println("@Override");
		ps.println(methodIndent+"public Message<"+table.getVoName()+"> select(" +table.getPk().getFieldType()+ " id){" );
		ps.println(methodBodyIndent+table.getVoName() +" vo = "+adapterName+".select(id);"); 
		ps.println(methodBodyIndent+"return Message.success(vo);");
		ps.println(methodIndent+"}");
		
		ps.println("@Override");
		ps.println(methodIndent+"public Message<Page<"+table.getVoName()+">> limit(" +table.getLimitBoName()+ " limitBo){" );
		ps.println(methodBodyIndent+"Page<"+table.getVoName() +"> page = "+adapterName+".limit(limitBo);");
		ps.println(methodBodyIndent+"return Message.success(page);");
		ps.println(methodIndent+"}");
		
	}
}
