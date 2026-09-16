# ===========================
# USERS - FCM Tokens
# ===========================

# Adds a FCM token to the authenticated user
PUT http://localhost:8080/users/me/fcm-tokens
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "deviceId": "<string>",
    "token": "<string>"
}

HTTP 200

---

# Deletes a FCM token from the authenticated user
DELETE http://localhost:8080/users/me/fcm-tokens
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "deviceId": "<string>",
    "token": "<string>"
}

HTTP 204

---

# ===========================
# USERS - Favorites
# ===========================

# Favorites a deal
PUT http://localhost:8080/users/me/favorites/{{dealId}}
Authorization: Bearer <Bearer Token>

HTTP 200

---

# Unfavorites a deal
DELETE http://localhost:8080/users/me/favorites/{{dealId}}
Authorization: Bearer <Bearer Token>

HTTP 204

---

# Returns the deals favorited by the authenticated user
GET http://localhost:8080/users/me/favorites?page=0&size=20&sort=<string>&sort=<string>
Authorization: Bearer <Bearer Token>

HTTP 200

---

# ===========================
# USERS - Blocks
# ===========================

# Blocks a user
PUT http://localhost:8080/users/me/blocks/{{id}}
Authorization: Bearer <Bearer Token>

HTTP 200

---

# Unblocks a user
DELETE http://localhost:8080/users/me/blocks/{{id}}
Authorization: Bearer <Bearer Token>

HTTP 204

---

# Returns the users blocked by the authenticated user
GET http://localhost:8080/users/me/blocks?page=0&size=20&sort=<string>&sort=<string>
Authorization: Bearer <Bearer Token>

HTTP 200

---

# ===========================
# USERS - Me
# ===========================

# Returns the authenticated user
GET http://localhost:8080/users/me
Authorization: Bearer <Bearer Token>

HTTP 200

---

# Updates the authenticated user
PATCH http://localhost:8080/users/me
Authorization: Bearer <Bearer Token>
Content-Type: application/json-patch+json
"<object>"

HTTP 200

---

# Returns the deals posted by the authenticated user
GET http://localhost:8080/users/me/deals?page=0&size=20&sort=<string>&sort=<string>
Authorization: Bearer <Bearer Token>

HTTP 200

---

# ===========================
# USERS - By ID
# ===========================

# Finds user by ID
GET http://localhost:8080/users/{{id}}

HTTP 200

---

# Reports a user
POST http://localhost:8080/users/{{id}}/reports
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "reasons": [
        "<string>",
        "<string>"
    ],
    "message": "<string>"
}

HTTP 201

---

# Finds user by ID (extended)
GET http://localhost:8080/users/{{id}}/extended

HTTP 200

---

# Returns the number of comments posted by a user
GET http://localhost:8080/users/{{id}}/comment-count

HTTP 200

---

# ===========================
# USERS - Search
# ===========================

# Finds user by uid
GET http://localhost:8080/users/search/findByUid?uid=<string>
Authorization: Bearer <Bearer Token>

HTTP 200

---

# Finds user by email
GET http://localhost:8080/users/search/findByEmail?email=<string>
Authorization: Bearer <Bearer Token>

HTTP 200

---

# Returns all users
GET http://localhost:8080/users?page=0&size=20
Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg3NzQ4NTAwMmYwNWJlMDI2N2VmNDU5ZjViNTEzNTMzYjVjNThjMTIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYmFjay1kZWFscyIsImF1ZCI6ImJhY2stZGVhbHMiLCJhdXRoX3RpbWUiOjE3NTE2MDEwNDksInVzZXJfaWQiOiJiOTF6Q0JsTjd5TkRwaDdtVDlYUmlQQWFXR2QyIiwic3ViIjoiYjkxekNCbE43eU5EcGg3bVQ5WFJpUEFhV0dkMiIsImlhdCI6MTc1MTYwMTA0OSwiZXhwIjoxNzUxNjA0NjQ5LCJlbWFpbCI6InB1bWFzZW1qQGhvdG1haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7ImVtYWlsIjpbInB1bWFzZW1qQGhvdG1haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.OZZkGz2dYTZVOsWX5ydLN3vMB5LT0-HX0TiKeWMtgY_hfta93xX8JNXjuB8kh-9k7v8k3SoubB0h4Hj7jZ88MLAghikJEw4ATgl2cpJHZUCq_bVCQT1ZYmVerUe4-tYMqT1jbIvngPCuAjXJ2k5TqL_GAfXti7sxxehvSufg3e0a0-x7Kuyc6uUugoWUneUp72ikPh6FEPuQEdRSWd6_nbWrC_bb4UkS8lPI-AUzFSyMSvhBxQxBatWHJgSNN0j7NjqVAUekrUHuVgnk2SpJk87UwqGehH9xGZCkfd_qRfwHSyi8w-dK2O2A-iMsM4J-gLxxI_5iNzDtQBNEkdn12g

HTTP 200

---

# Creates a user
POST http://localhost:8080/users
Content-Type: application/json
{
    "uid": "FFUpcm8hfZUZDWg6GHunuFNQw8u2",
    "avatar": "https://www.gravatar.com/avatar",
    "email": "pumasbra@hotmail.com"
}

HTTP 201

---

# ===========================
# STORES
# ===========================

# Finds store by ID
GET http://localhost:8080/stores/{{id}}
Authorization: Bearer <Bearer Token>

HTTP 200

---

# Updates an existing store
PUT http://localhost:8080/stores/{{id}}
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "logo": "<string>",
    "name": "<string>"
}

HTTP 200

---

# Deletes an existing store
DELETE http://localhost:8080/stores/{{id}}
Authorization: Bearer <Bearer Token>

HTTP 204

---

# Returns all stores
GET http://localhost:8080/stores?page=0&size=20

HTTP 200

---

# Creates a store
POST http://localhost:8080/stores
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "logo": "<string>",
    "name": "<string>"
}

HTTP 201

---

# ===========================
# ROLES
# ===========================

# Adds a role to a user
PUT http://localhost:8080/roles?uid=b91zCBlN7yNDph7mT9XRiPAaWGd2&role=ROLE_SUPER
Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE0YTEwZGVjZTk4MzY2ZDZmNjNlMTY3Mjg2YWU5YjYxMWQyYmFhMjciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYmFjay1kZWFscyIsImF1ZCI6ImJhY2stZGVhbHMiLCJhdXRoX3RpbWUiOjE3NDk4NzMxODksInVzZXJfaWQiOiJiOTF6Q0JsTjd5TkRwaDdtVDlYUmlQQWFXR2QyIiwic3ViIjoiYjkxekNCbE43eU5EcGg3bVQ5WFJpUEFhV0dkMiIsImlhdCI6MTc0OTg3MzE4OSwiZXhwIjoxNzQ5ODc2Nzg5LCJlbWFpbCI6InB1bWFzZW1qQGhvdG1haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7ImVtYWlsIjpbInB1bWFzZW1qQGhvdG1haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.ZwjoLYjkIqmfddcwUz3uWr_pLWc_0uJUvcEEp1-D4GkVUuCd68jXal2_CNVEi7SUsXhCBArhfpCWpTzhr6oNAFLNxqOodEyC1X00GMgd1T2U5oOepLO3PKW-8QEVwetnPZD4l7h84C3fC_Y8Gys2kYq55K3lSEDzkar6WRBX_ME0NBkBarUFut-JRL0VRMvwr0zbzKQHbVMPYcWb5V2pF6r2oaTuD3UnKKjo3bjd4onKZ5BxY2ZFKXl4TzaGKpAs-fQVJ6kW_LXzBHmbGv2ogd5JnhRRR__q1EI4Fyne6CPzbgjuV9HxuwGdqX8dmqKwFwPQ_zXlNBvVl5vL8dSCkg

HTTP 200

---

# Deletes a role from a user
DELETE http://localhost:8080/roles?uid=<string>&role=<string>
Authorization: Bearer <Bearer Token>

HTTP 204

---

# ===========================
# DEALS - Votes
# ===========================

# Casts vote to a deal
PUT http://localhost:8080/deals/{{id}}/votes
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "voteType": "<string>"
}

HTTP 200

---

# Deletes the vote from a deal
DELETE http://localhost:8080/deals/{{id}}/votes
Authorization: Bearer <Bearer Token>

HTTP 200

---

# ===========================
# DEALS - Comments
# ===========================

# Returns the comments posted to a deal
GET http://localhost:8080/deals/{{id}}/comments?page=0&size=20&sort=<string>&sort=<string>

HTTP 200

---

# Adds a comment to a deal
POST http://localhost:8080/deals/{{id}}/comments
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "message": "<string>"
}

HTTP 201

---

# Reports a deal comment
POST http://localhost:8080/deals/{{id}}/comments/{{commentId}}/reports
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "reasons": [
        "<string>",
        "<string>"
    ],
    "message": "<string>"
}

HTTP 201

---

# ===========================
# DEALS - CRUD
# ===========================

# Finds a deal by ID
GET http://localhost:8080/deals/{{id}}

HTTP 200

---

# Updates a deal
PUT http://localhost:8080/deals/{{id}}
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "category": "<string>",
    "coverPhoto": "<string>",
    "dealUrl": "<string>",
    "description": "<string>",
    "originalPrice": "<double>",
    "price": "<double>",
    "store": "<string>",
    "title": "<string>",
    "photos": [
        "<string>",
        "<string>"
    ]
}

HTTP 200

---

# Deletes a deal
DELETE http://localhost:8080/deals/{{id}}
Authorization: Bearer <Bearer Token>

HTTP 204

---

# Patches a deal
PATCH http://localhost:8080/deals/{{id}}
Authorization: Bearer <Bearer Token>
Content-Type: application/json-patch+json
"<object>"

HTTP 200

---

# Reports a deal
POST http://localhost:8080/deals/{{id}}/reports
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "reasons": [
        "<string>",
        "<string>"
    ],
    "message": "<string>"
}

HTTP 201

---

# Returns the number of comments for a deal
GET http://localhost:8080/deals/{{id}}/comment-count

HTTP 200

---

# ===========================
# DEALS - Search
# ===========================

# Most liked active deals
GET http://localhost:8080/deals/search/mostLikedActive?page=0&size=20&sort=<string>&sort=<string>

HTTP 200

---

# Latest active deals
GET http://localhost:8080/deals/search/latestActive?page=0&size=20&sort=<string>&sort=<string>

HTTP 200

---

# Deals by store ID
GET http://localhost:8080/deals/search/byStoreId?storeId=<string>&page=0&size=20&sort=<string>&sort=<string>

HTTP 200

---

# Deals by category
GET http://localhost:8080/deals/search/byCategory?category=<string>&page=0&size=20&sort=<string>&sort=<string>

HTTP 200

---

# Count deals by store ID
GET http://localhost:8080/deals/count/byStoreId?storeId=<string>

HTTP 200

---

# Count deals by posted by
GET http://localhost:8080/deals/count/byPostedBy?postedBy=<string>

HTTP 200

---

# Returns all deals
GET http://localhost:8080/deals?page=0&size=20
Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE0YTEwZGVjZTk4MzY2ZDZmNjNlMTY3Mjg2YWU5YjYxMWQyYmFhMjciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYmFjay1kZWFscyIsImF1ZCI6ImJhY2stZGVhbHMiLCJhdXRoX3RpbWUiOjE3NDk4NzMxODksInVzZXJfaWQiOiJiOTF6Q0JsTjd5TkRwaDdtVDlYUmlQQWFXR2QyIiwic3ViIjoiYjkxekNCbE43eU5EcGg3bVQ5WFJpUEFhV0dkMiIsImlhdCI6MTc0OTg3MzE4OSwiZXhwIjoxNzQ5ODc2Nzg5LCJlbWFpbCI6InB1bWFzZW1qQGhvdG1haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7ImVtYWlsIjpbInB1bWFzZW1qQGhvdG1haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.ZwjoLYjkIqmfddcwUz3uWr_pLWc_0uJUvcEEp1-D4GkVUuCd68jXal2_CNVEi7SUsXhCBArhfpCWpTzhr6oNAFLNxqOodEyC1X00GMgd1T2U5oOepLO3PKW-8QEVwetnPZD4l7h84C3fC_Y8Gys2kYq55K3lSEDzkar6WRBX_ME0NBkBarUFut-JRL0VRMvwr0zbzKQHbVMPYcWb5V2pF6r2oaTuD3UnKKjo3bjd4onKZ5BxY2ZFKXl4TzaGKpAs-fQVJ6kW_LXzBHmbGv2ogd5JnhRRR__q1EI4Fyne6CPzbgjuV9HxuwGdqX8dmqKwFwPQ_zXlNBvVl5vL8dSCkg

HTTP 200

---

# Creates a deal
POST http://localhost:8080/deals
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "category": "<string>",
    "coverPhoto": "<string>",
    "dealUrl": "<string>",
    "description": "<string>",
    "originalPrice": "<double>",
    "price": "<double>",
    "store": "<string>",
    "title": "<string>",
    "photos": [
        "<string>",
        "<string>"
    ]
}

HTTP 201

---

# Deal suggestions
GET http://localhost:8080/deals/suggestions?query=<string>

HTTP 200

---

# Deal searches
GET http://localhost:8080/deals/searches?query=<string>&categories=<string>&categories=<string>&prices=<string>&prices=<string>&stores=<string>&stores=<string>&hideExpired=false&sortBy=<string>&order=asc&page=0&size=20&sort=<string>&sort=<string>

HTTP 200

---

# ===========================
# CATEGORIES
# ===========================

# Finds category by ID
GET http://localhost:8080/categories/{{id}}
Authorization: Bearer <Bearer Token>

HTTP 200

---

# Updates a category
PUT http://localhost:8080/categories/{{id}}
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "category": "<string>",
    "iconFontFamily": "<string>",
    "iconLigature": "<string>",
    "names": "<object>",
    "parent": "<string>"
}

HTTP 200

---

# Deletes a category
DELETE http://localhost:8080/categories/{{id}}
Authorization: Bearer <Bearer Token>

HTTP 204

---

# Returns all categories
GET http://localhost:8080/categories?page=0&size=20

HTTP 200

---

# Returns category tags
GET http://localhost:8080/categories/tags?page=0&size=20

HTTP 200

---

# Creates a category
POST http://localhost:8080/categories
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "category": "<string>",
    "iconFontFamily": "<string>",
    "iconLigature": "<string>",
    "names": "<object>",
    "parent": "<string>"
}

HTTP 201

---

# ===========================
# NOTIFICATIONS
# ===========================

# Sends a notification
POST http://localhost:8080/notifications
Authorization: Bearer <Bearer Token>
Content-Type: application/json
{
    "data": "<object>",
    "tokens": [
        "<string>",
        "<string>"
    ],
    "title": "<string>",
    "titleLocKey": "<string>",
    "body": "<string>",
    "bodyLocKey": "<string>",
    "image": "<string>",
    "titleLocArgs": [
        "<string>",
        "<string>"
    ],
    "bodyLocArgs": [
        "<string>",
        "<string>"
    ]
}

HTTP 200
