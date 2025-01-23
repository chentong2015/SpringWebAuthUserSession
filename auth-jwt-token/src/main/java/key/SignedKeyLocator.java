package key;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Locator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

// TODO. 定位Secret Key从何而来 ?
//  通过请求的Header(JwsHeader, JweHeader)头部信息获取Key
public class SignedKeyLocator implements Locator<Key> {

    @Override
    public Key locate(Header header) {
        return resolveSigningKey((JwsHeader) header);
    }

    private Key resolveSigningKey(JwsHeader jwsHeader) {
        String keyId = jwsHeader.getKeyId();
        String x5t = jwsHeader.get("x5t").toString();
        Certificate certificate= getSigningCertificate(x5t);
        return certificate.getPublicKey();
    }

    // 基于Key ID来获取包含Secret Key的Certificate
    public X509Certificate getSigningCertificate(String keyId) {
        // Get Key value based on Key ID
        String x509Base64 = "TCCAeWgAwIBAgIIOdwuMdHVGk";
        byte[] x509bytes = Base64.getDecoder().decode(x509Base64);
        InputStream inputStream = new ByteArrayInputStream(x509bytes);
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return  (X509Certificate) factory.generateCertificate(inputStream);
        } catch (CertificateException e) {
            return null;
        }
    }
}
