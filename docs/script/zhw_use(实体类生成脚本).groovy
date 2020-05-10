import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import java.text.SimpleDateFormat

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

typeMapping = [
        (~/(?i)int/)                      : "Integer",
        (~/(?i)float|double|decimal|real/): "Double",
        (~/(?i)datetime|timestamp/)       : "Date",
        (~/(?i)date/)                     : "Date",
        (~/(?i)time/)                     : "Date",
        (~/(?i)/)                         : "String"
]

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable }.each { generate(it, dir) }
}

// 获取包所在文件夹路径
def getPackageName(dir) {
    return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}

packageName = ""
//目标目录下生成文件
def generate(table, dir) {
    def tableComment = getTableComment(table)
    if (tableComment == null || "".equals(tableComment)) {
        tableComment = ""
    }
    packageName = getPackageName(dir)
    def className = javaName(table.getName(), true)
    className += "PO"
    def fields = calcFields(table)
    //表名
    def tableName = getTableName(table)
    PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, className + ".java")), "utf-8"))
    output.withPrintWriter { out -> generate(out, className, fields, tableName, tableComment) }
}

def generate(out, className, fields, tableName, tableComment) {
    out.println "package $packageName"
    out.println "import java.io.Serializable;"
    out.println "import javax.persistence.*;"
    out.println "import java.util.Date;"
    out.println "import com.fasterxml.jackson.annotation.JsonFormat;"
    out.println "import org.springframework.format.annotation.DateTimeFormat;"
    out.println "import org.hibernate.annotations.DynamicInsert;"
    out.println "import org.hibernate.annotations.DynamicUpdate;"
    out.println ""
    out.println ""
    out.println "@Entity"
    out.println "@Table(name = \"" + tableName + "\")"
    out.println "@DynamicInsert\n" + "@DynamicUpdate\n"
    out.println ""
    out.println "public class $className  implements Serializable {"
    fields.each() {
//        if (it.columnName.toLowerCase() != "id") {
        if (isNotEmpty(it.comment)) {
            out.println "\t/*${it.comment}*/"
        }
        if (it.annos != "") out.println "  ${it.annos}"
        if ((it.columnName == "id") || (it.columnName == "ID")) {
            out.println "@Id"
            out.println "@GeneratedValue(strategy = GenerationType.IDENTITY)"
        }
        out.println "\t@Column(name=\"${it.columnName}\")"
        if (it.type == "Date") {
            out.println "\t@JsonFormat(pattern=\"yyyy-MM-dd\",timezone = \"GMT+8\")"
            out.println "\t@DateTimeFormat(pattern = \"yyyy-MM-dd\")"
        }
        out.println "\tprivate ${it.type} ${it.name};"
//        }
    }
    out.println ""
    fields.each() {
//        if (it.name.toLowerCase() != "id") {
        out.println "\tpublic ${it.type} get${it.name.capitalize()}() {"
        out.println "\t\treturn ${it.name};"
        out.println "\t}"
        out.println ""
        out.println "\tpublic void set${it.name.capitalize()}(${it.type} ${it.name}) {"
        out.println "\t\tthis.${it.name} = ${it.name};"
        out.println "\t}"
        out.println ""
//        }
    }
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        fields += [[
                           name      : javaName(col.getName(), false),
                           columnName: col.getName(),
                           type      : typeStr,
                           comment   : col.getComment(),
                           annos     : ""]]
    }
}

def getTableComment(table) {
    return table.getComment();
}

def getTableName(table) {
    return table.getName();
}


def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

def isNotEmpty(content) {
    return content != null && content.toString().trim().length() > 0
}

def getNowDateYMS() {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")// 设置日期格式
    return df.format(new Date())
}