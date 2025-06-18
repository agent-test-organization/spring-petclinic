# Slack Integration

This project includes a Slack integration to send notifications for copilot activities and PR reviews.

## Configuration

Add the following properties to your `application.properties` or `application.yml`:

```properties
# Slack Integration
slack.enabled=true
slack.webhook-url=https://hooks.slack.com/services/YOUR/WEBHOOK/URL
slack.default-channel=copilot-review
```

## Endpoints

The Slack integration provides the following REST endpoints:

### POST /api/notifications/plan-ready
Sends a notification when Copilot finishes creating a high-level plan that needs review.

**Request Body:**
```json
{
  "prUrl": "https://github.com/org/repo/pull/123"
}
```

**Response:**
```
Plan ready notification sent
```

### POST /api/notifications/pr-ready
Sends a notification when a PR is ready for review.

**Request Body:**
```json
{
  "prUrl": "https://github.com/org/repo/pull/123"
}
```

**Response:**
```
PR ready notification sent
```

## Slack Webhook Setup

1. Go to your Slack workspace
2. Create a new app or use an existing one
3. Enable Incoming Webhooks
4. Create a webhook for the desired channel (e.g., `#copilot-review`)
5. Copy the webhook URL and set it in your configuration

## Example Usage

```bash
# Notify when plan is ready
curl -X POST http://localhost:8080/api/notifications/plan-ready \
  -H "Content-Type: application/json" \
  -d '{"prUrl": "https://github.com/your-org/your-repo/pull/123"}'

# Notify when PR is ready
curl -X POST http://localhost:8080/api/notifications/pr-ready \
  -H "Content-Type: application/json" \
  -d '{"prUrl": "https://github.com/your-org/your-repo/pull/456"}'
```

## Security Notes

- Keep your webhook URL secure and do not commit it to version control
- Consider using environment variables or external configuration for webhook URLs
- The integration uses asynchronous HTTP calls to avoid blocking the main application