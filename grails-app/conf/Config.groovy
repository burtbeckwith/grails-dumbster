log4j = {
	error 'org.codehaus.groovy.grails',
	      'org.springframework',
	      'org.hibernate',
	      'net.sf.ehcache.hibernate'
	debug 'grails.plugin.dumbster'
}

environments {
	test {
		dumbster.enabled = true
//		dumbster.port = 1200
//		grails.mail.port = 1200
	}
}
