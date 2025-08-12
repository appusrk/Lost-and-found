from flask import Flask, request
from twilio.twiml.messaging_response import MessagingResponse
import requests
import json

app = Flask(__name__)

JAVA_BACKEND_URL = "http://localhost:8080/message"  # Spring Boot endpoint

@app.route("/whatsapp", methods=["POST"])
def reply():
    msg = request.form.get("Body")
    sender = request.form.get("From")

    # Send message + sender to Spring Boot
    try:
        requests.post(
            JAVA_BACKEND_URL,
            data=json.dumps({"from": sender, "message": msg}),
            headers={"Content-Type": "application/json"}
        )
    except Exception as e:
        print("Error forwarding to Java backend:", e)

    # WhatsApp auto-response
    resp = MessagingResponse()
    response = resp.message()

    if "lost" in msg.lower():
        response.body("I’m sorry to hear that. Tell me what you lost and where.")
    elif "found" in msg.lower():
        response.body("Thanks for reporting! Tell me what you found and where.")
    else:
        response.body("Hi! Type 'lost' or 'found' to get started.")

    return str(resp)

if __name__ == '__main__':
    app.run(host='0.0.0.0')

