package cs408.purdueclassassistant;

/**
 * Created by tannermcrae on 2/19/14.
 */

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MyPurdueScraper {

    public static String scrapeSchedule(final String username, final String password) {
        String[] schedule = new String[10];

        String errorMessage = "Error";

        HttpsURLConnection https = null;
        URL url = null;
        InputStream in = null;

        // Set default Authenticator's params to username and password
        Authenticator.setDefault(new Authenticator() {
            boolean alreadyTriedAuthenticating = false;

            protected PasswordAuthentication getPasswordAuthentication() {
                if (!alreadyTriedAuthenticating) {
                    alreadyTriedAuthenticating = true;
                    return new PasswordAuthentication(username, password.toCharArray());
                } else {
                    return null;
                }
            }
        });

        try {
            trustAllHosts();
            url = new URL("https://www.mypurdue.purdue.edu");
            https = (HttpsURLConnection)url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            in = https.getInputStream();
        } catch (Exception e) {
            // Too lazy
            in = new ByteArrayInputStream(Charset.forName("UTF-16").encode(errorMessage).array());

        }
        String myString = convertStreamToString(in);


        return myString;
    }

    // Stupid Scanner trick
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - don't check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
