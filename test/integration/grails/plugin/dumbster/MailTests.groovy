package grails.plugin.dumbster

import com.dumbster.smtp.SmtpMessage

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class MailTests extends GroovyTestCase {

	def mailService
	def dumbster

	protected void setUp() {
		super.setUp()
		dumbster.reset()
	}

	void testMail() {

		mailService.sendMail {
			to "tester@test.com"
			from "grails@grails.org"
			subject "test1"
			text "This is a test"
		}

		assert 1 == dumbster.messageCount

		SmtpMessage email = dumbster.messages[0]
		assert 'test1' == email.subject
		assert 'This is a test' == email.body
	}

	void testReset() {

		mailService.sendMail {
			to "tester@test.com"
			from "grails@grails.org"
			subject "test2"
			text "This is a test"
		}

		assert 1 == dumbster.messageCount

		SmtpMessage email = dumbster.messages[0]
		assert 'test2' == email.subject
		assert 'This is a test' == email.body
	}

	void testMultipleMessages() {

		mailService.sendMail {
			to "tester@test.com"
			from "grails@grails.org"
			subject "test3a"
			text "This is a test"
		}

		mailService.sendMail {
			to "tester@test.com"
			from "grails@grails.org"
			subject "test3b"
			text "This is a test"
		}

		assert 2 == dumbster.messageCount

		def expectedSubjects = dumbster.messages.collect { it.subject }
		assertEquals(expectedSubjects, ['test3a', 'test3b'])
	}

	void testExtraRecipients() {

		mailService.sendMail {
			to "to1@test.com", "to2@test.com"
			cc "cc1@test.com", "cc2@test.com"
			from "grails@grails.org"
			subject "test3"
			text "This is a test"
		}

		assert 1 == dumbster.messageCount

		SmtpMessage email = dumbster.messages[0]
		assert 'test3' == email.subject
		assertNotNull email.date
		assert 'to1@test.com' == email.to
		assert ['to1@test.com', 'to2@test.com'] == email.tos
		assert 'cc1@test.com' == email.cc
		assert ['cc1@test.com', 'cc2@test.com'] == email.ccs
	}
}
