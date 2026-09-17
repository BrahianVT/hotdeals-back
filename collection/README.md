# Hotdeals API — Hurl Collection

HTTP integration tests for the hotdeals REST API, migrated from Postman.

## Requirements

- [Hurl](https://hurl.dev/docs/installation.html) installed (`brew install hurl` / `winget install hurl`)

---

## Variables

All files use injected variables. Pass them via `--variable` or a `--variables-file`.

| Variable  | Description                  | Required by               |
|-----------|------------------------------|---------------------------|
| `baseUrl` | API base URL                 | All files                 |
| `token`   | Firebase Bearer JWT token    | Authenticated endpoints   |
| `query`   | Search query string          | `deals-search.hurl`       |

### Using a variables file (recommended)

Create a `vars.env` file in this folder:

```env
baseUrl=https://api.promoabastos.com
token=YOUR_FIREBASE_JWT_TOKEN
```

Then run any file with:

```bash
hurl --variables-file collection/vars.env collection/<file>.hurl
```
or execute them directly with variables name:
hurl --variable baseUrl=https://api.promoabastos.com --variable token=token collection/deals-get-all.hurl
hurl --variable baseUrl=https://api.promoabastos.com --variable query=Aceite --variable token=token collection/deals-search.hurl


---

## Files

### `hotdeals.hurl`
Full collection — all endpoints in one file grouped by resource.

```bash
hurl --variables-file collection/vars.env collection/hotdeals.hurl
```

> Run a specific entry (e.g. entry #3):
> ```bash
> hurl --from-entry 3 --to-entry 3 --variables-file collection/vars.env collection/hotdeals.hurl
> ```

---

### `deals-get-all.hurl`
Returns all deals (paginated). Requires authentication.

**Endpoint:** `GET /deals?page=0&size=20`

```bash
hurl --variable baseUrl=https://api-dev.promoabastos.com \
     --variable token=YOUR_TOKEN \
     collection/deals-get-all.hurl
```

---

### `deals-search.hurl`
Searches deals by query string. Requires authentication.

**Endpoint:** `GET /deals/searches?query={{query}}`

```bash
hurl --variable baseUrl=https://api-dev.promoabastos.com \
     --variable token=YOUR_TOKEN \
     --variable query=monitor \
     collection/deals-search.hurl
```

---

## Tips

- Add `--verbose` to see full request/response details:
  ```bash
  hurl --verbose --variables-file collection/vars.env collection/deals-search.hurl
  ```

- Add `--test` to run in test mode (shows pass/fail per entry):
  ```bash
  hurl --test --variables-file collection/vars.env collection/hotdeals.hurl
  ```
