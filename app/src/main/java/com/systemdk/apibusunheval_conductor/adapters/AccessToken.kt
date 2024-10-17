package com.systemdk.apibusunheval_conductor.adapters

import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

object AccessToken {

    private val firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging"

    fun getAccesToken(): String? {
        try {
            val jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"apibusunheval\",\n" +
                    "  \"private_key_id\": \"e91c9662ef3791eb0f93615b341d5bdd3ee0dd78\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDQM8fR3HoZX5z3\\niNWaWpjQZevhPdhC6HgJmGT3WzY7zNpZ/O6cPTtYiX8FBTaFNeD+iJRDUPsLLWyQ\\nLqqnx4/y7XDHcR7buSjjZmvlCPfv0vPfxb4dsFW+SS8odbT3uX8UYf6yWFHfcW0y\\nP+PbWdKoGl2hWHHEDrTk6Yo3nHu1Bxko5NJ1jCPxpNOpiNNIFKHhyyJQaZ7PQ/Pq\\n3r735drLfrMFGok1Vj0LqSfKGIXgg5LDcNoXzBrIAZ46ol+Li8YvUY6zNtcEd+iB\\nXBxiEShk6YHt6OIDy9G4oaESp1KIQX3cyNyhRrVY24B3cHGw6mx+gq+5p6OgESSY\\ncV3ML8LnAgMBAAECggEAVLGTqeOBybuSy0Ee/zS2cCPd0dkKFaGiXujevZ6dj7Lz\\n6eJRoD3qW8hG4uBtfr/doD0n7sBM9w2J2ZcaNbkg7gW3uHHkRyZpoyOBZYCB9019\\nhjxW65XVM1xq8g77yD/tmXmJlbwGxhnKfUPvKUY/QsHC78TeNo5y+KGl55xg2FDo\\nQOH1aLLbH9mHHS6LTzdLBMyJ3gfY2wOk5BJ5rjM/BrHk0hqU2spiMI4dNe/mALdl\\n8k9BanUnucbJa9rYBpxdPp3N8uOMdiNFXvDPn45OwfWZPc4Sub2k+/r3TxL5lEwY\\nElYVfJ0HFbl86ifHxUGO3vWYY/sNQxhHgFlEaKxZCQKBgQD0bbhhtQFWmMBSRpuv\\nE2srZxtmwXuHrx6dshjzsIFthjju74u2WOSKb84b8tpkVD2auf5DciNPCbaNiaSQ\\nMTbjfMDeR0pC80O9vGq8GUDQTCOAY8PxJmlSUqa++1AmcoxRg4ekSd8OvTh/Yihp\\nXJiA580f0tGjl268Jnnq9ye2bwKBgQDaDwauJ4DZy4WeLEbKjHjGhUq9n0k11zvD\\nuQQxqipGt4AzyoKHKQ+S678HNjw00H2yJvHeVoJoZUvf7Zh4ZG8++n9mKbgbRQ40\\nA7pEK1qR/Ph3NK2l0U5TRLyBv4M4+nuUBgmNAqIXGE+nCdGeeF1sRdFB5PgCmKEn\\nZ9o9Zi+3CQKBgQCdNhTpgyohMI5CXPe1W6AKgn61UKzLUG84rlwuINCeST5FL/2E\\ngxg7kj3W7BRbg3M8GXTYnIjpRS4NSwJ1W7IaDxtd3Cx/c0eWuaFM6lEtqEsNQR29\\n9R64vEBThgC/Od3Wb+rGWF0Hddzo0ZF8cvoDrxPX7Bi6R6QJkBXKcr0wMQKBgCsZ\\nFy/qnNvvDsyxYZh85Q4PoDMUPWsHzEcl1T9fq5VohU03NE80fWCDebEaNhIWNxG0\\nAs/39zRc+P8cPatl5lrjNbTigAHbxy/eoL0CeJcsEYu1/LAuSzXH+x4F1RC46R3b\\nh524LIM2CeAw355bmdNhhTdp4t7YfdmE/CxwevPJAoGATEKqOhjmQxThMTydGs2c\\ndiS7COGiAOP3y6Ooxe0GvDhzM4Q2u2bqxT9jUmnbVB+OI2N77EeHqgxWFimOl1I/\\nsTEalHAXAeDa+yd5b1u5TmLbsY2FxSBmq/gIHvbJwWjyfRuyGUJq2qGkqs8IE/pw\\nb3XlDArIBDQnLwFnYj2sqmo=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-r6lid@apibusunheval.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"114495320745826237074\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-r6lid%40apibusunheval.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}"

            val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))

            val gooleCredential = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(firebaseMessagingScope))

            gooleCredential.refresh()
            return gooleCredential.accessToken.tokenValue
        }catch (e: IOException){
            return null
        }
    }

}