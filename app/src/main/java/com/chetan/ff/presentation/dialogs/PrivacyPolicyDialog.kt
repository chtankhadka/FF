package com.chetan.ff.presentation.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun PrivacyPolicyDialog(
    onDismiss: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = {
        onDismiss(false)
    }, content = {
        Card(
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                        )
                    ) {
                        append("Welcome to ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("MOMO BAR Next In")
                    }
                })

                Text(
                    style = MaterialTheme.typography.titleMedium, text = "1. Information We Collect"
                )

                Text(
                    style = MaterialTheme.typography.titleSmall, text = "1.1. Personal Information:"
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "\nContact details(e.g., name, email address, phone number\nDelivery address"
                )

                Text(
                    style = MaterialTheme.typography.titleSmall, text = "1.2. Usage Data:"
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "We may collect non-personal information about how you use our App, such as the pages you visit, the time and date of your visits, and your device information."
                )

                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "2. How We Use Your Information"
                )


                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "2.1. We use your information to provide you with our services, including processing forms and submissions."
                )


                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "2.2. We may use your information to communicate with you, respond to your inquiries, and send you updates related to the App."
                )

                Text(
                    style = MaterialTheme.typography.titleMedium, text = "3. Data Security"
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "3.1. We implement reasonable security measures to protect your data from unauthorized access, disclosure, alteration, or destruction."
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "3.2. While we strive to protect your personal information, please be aware that no data transmission over the internet is entirely secure, and we cannot guarantee absolute security."
                )
                Text(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("3.3 No Data Shared with third Parties")
                    }
                    append("This app doesn’t share users personal data with other companies or organizations.")
                })

                Text(
                    style = MaterialTheme.typography.titleMedium, text = "4. User Rights"
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "4.1. You can access and review your personal information that we have collected."
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "4.2. You may request corrections to inaccurate or incomplete data."
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "4.3. You have the right to request the deletion of your personal information, subject to legal requirements."
                )


                Text(
                    style = MaterialTheme.typography.titleMedium, text = "5. Third-Party Links"
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Our App may contain links to third-party websites or services that we do not control. This Privacy Policy does not apply to those sites, so please review their privacy policies."
                )



                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "6. Changes to this Privacy Policy"
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "We may update this Privacy Policy to reflect changes in our practices or for other operational, legal, or regulatory reasons. We will notify you of any significant changes."
                )

                Text(
                    style = MaterialTheme.typography.titleMedium, text = "7. Contact Us"
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "If you have questions, concerns, or requests related to your data or this Privacy Policy, please contact us at 9745904088."
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "By using our App, you agree to the terms outlined in this Privacy Policy."
                )




                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    onDismiss(false)
                }) {
                    Text(text = "Confirm")
                }
            }

        }

    })
}