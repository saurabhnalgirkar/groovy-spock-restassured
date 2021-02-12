// This is the spock config file where the extensions that Spock supports can be defined
// http://spockframework.org/spock/docs/1.1/extensions.html

// The reporting extension used is defined here
// https://github.com/renatoathaydes/spock-reports

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

spockReports {

    def format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
    def date = format.format(LocalDateTime.now())

    // Show the source code for each block
    set 'com.athaydes.spockframework.report.showCodeBlocks': true

    // Output directory (where the spock reports will be created) - relative to working directory
    set 'com.athaydes.spockframework.report.outputDir': 'target/spock-reports'

    // Set the name of the project under test so it can be displayed in the report
    set 'com.athaydes.spockframework.report.projectName': 'Ataccama Calc Task | '

    // Set the version of the project under test so it can be displayed in the report
    set 'com.athaydes.spockframework.report.projectVersion': '' + date.toString()

}