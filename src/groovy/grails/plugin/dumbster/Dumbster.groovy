package grails.plugin.dumbster

import com.dumbster.smtp.SimpleSmtpServer
import com.dumbster.smtp.SmtpMessage

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class Dumbster {

	def grailsApplication
	def mailSender

	protected SimpleSmtpServer server

	/**
	 * Starts the server; called by Spring, so shouldn't be called directly.
	 */
	void start() {

		def conf = grailsApplication.config.dumbster

		int port
		if (conf.port instanceof Number) {
			port = conf.port
		}
		else {
			port = 1025
			while (true) {
				try {
					new ServerSocket(port).close()

					// update the mail plugin's JavaMailSender if available
					mailSender?.port = port
					break
				}
				catch (IOException e) {
					port++
					if (port > 10000) {
						throw new RuntimeException('Cannot find open port for Dumbster SMTP server')
					}
				}
			}
		}

		server = SimpleSmtpServer.start(port)
	}

	/**
	 * Stops the server; called by Spring, so shouldn't be called directly.
	 */
	void stop() {
		server.stop()
	}

	/**
	 * Remove all sent emails. Call this in the setUp() method in your integration tests.
	 */
	void reset() {
		for (Iterator iter = server.receivedEmail; iter.hasNext(); ) {
			iter.next()
			iter.remove()
		}
	}

	/**
	 * Check if stopped.
	 * @return <code>true</code> if the server was stopped (or never started)
	 */
	boolean isStopped() { server.stopped }

	/**
	 * Get all current messages.
	 * @return the messages
	 */
	List<SmtpMessage> getMessages() { server.receivedEmail.collect { it } }

	/**
	 * Get the number of sent messages.
	 * @return the number
	 */
	int getMessageCount() { server.receivedEmailSize }
}
