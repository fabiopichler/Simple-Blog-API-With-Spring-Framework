/*-------------------------------------------------------------------------------

Copyright (c) 2023 Fábio Pichler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

-------------------------------------------------------------------------------*/

package net.fabiopichler.simpleblogapiwithspringframework.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class EncryptorUtil {

    private final String secretKey;
    private final String salt;
    private final TextEncryptor textEncryptor;

    public EncryptorUtil(
            @Value("${security.encryptor.secret-key}") String secretKey,
            @Value("${security.encryptor.salt}") String salt) {
        this.secretKey = secretKey;
        this.salt = salt;
        this.textEncryptor = Encryptors.text(secretKey, salt);
    }

    public String encrypt(String text) {
        String encrypted = textEncryptor.encrypt(text);

        return Base64.getEncoder().encodeToString(encrypted.getBytes(StandardCharsets.UTF_8));
    }

    public String decrypt(String encryptedText) {
        String decoded = new String(Base64.getDecoder().decode(encryptedText), StandardCharsets.UTF_8);

        return textEncryptor.decrypt(decoded);
    }
}
