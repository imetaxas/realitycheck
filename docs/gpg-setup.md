# GPG Key Setup for Maven Central Signing

Reference guide for generating and managing the GPG key used to sign artifacts
before publishing to Maven Central.

## Prerequisites

```bash
brew install gnupg      # macOS
gpg --version           # verify installation
```

---

## 1. Generate the key

```bash
gpg --full-generate-key
```

Answer the prompts:

| Prompt | Answer |
|---|---|
| Kind of key | `1` (RSA and RSA) |
| Keysize | `4096` |
| Expiry | `0` (does not expire) |
| Real name | Your name |
| Email | Must match your Sonatype Central Portal account |
| Comment | Leave blank |
| Passphrase | Choose a strong passphrase — this is `GPG_PASSPHRASE` |

---

## 2. Find your key ID

```bash
gpg --list-secret-keys --keyid-format LONG
```

Example output:

```
sec   rsa4096/3AA5C34371567BD2 2024-01-15 [SC]
      ABCDEF1234567890ABCDEF123AA5C34371567BD2
uid           [ultimate] Your Name <you@example.com>
ssb   rsa4096/4BB6D45482678CE3 2024-01-15 [E]
```

Your `KEY_ID` is the 16-char string after the `/` on the `sec` line:
`3AA5C34371567BD2`

---

## 3. Export the private key (→ `GPG_PRIVATE_KEY` secret)

```bash
gpg --armor --export-secret-keys 3AA5C34371567BD2
```

Copy the **entire output** including the header and footer:

```
-----BEGIN PGP PRIVATE KEY BLOCK-----
...
-----END PGP PRIVATE KEY BLOCK-----
```

Paste this block as the `GPG_PRIVATE_KEY` secret in the `maven-central`
GitHub environment.

---

## 4. Publish the public key to a keyserver

Maven Central verifies release signatures against public keyservers.
This step is required — without it the release will be rejected.

```bash
gpg --keyserver keyserver.ubuntu.com --send-keys 3AA5C34371567BD2
```

Verify it propagated (may take a few minutes):

```bash
gpg --keyserver keyserver.ubuntu.com --search-keys you@example.com
```

---

## 5. Store secrets in GitHub

Go to:
```
https://github.com/imetaxas/realitycheck/settings/environments
→ maven-central environment → Secrets
```

| Secret name | Value |
|---|---|
| `GPG_PRIVATE_KEY` | Full `-----BEGIN PGP PRIVATE KEY BLOCK-----` block from Step 3 |
| `GPG_PASSPHRASE` | The passphrase chosen in Step 1 |

---

## Maintenance

### List all keys on this machine

```bash
gpg --list-secret-keys --keyid-format LONG
```

### Export the public key (safe to share)

```bash
gpg --armor --export 3AA5C34371567BD2
```

### Extend expiry on an existing key

```bash
gpg --edit-key 3AA5C34371567BD2
# at the gpg> prompt:
expire
# follow prompts, then:
save
```

### Delete a key from local keyring

```bash
gpg --delete-secret-and-public-key 3AA5C34371567BD2
```

### Revoke a compromised key

```bash
gpg --gen-revoke 3AA5C34371567BD2 > revoke.asc
gpg --import revoke.asc
gpg --keyserver keyserver.ubuntu.com --send-keys 3AA5C34371567BD2
```

---

## Troubleshooting

**`gpg: signing failed: No secret key`**
The key ID in the Maven GPG plugin config does not match a key in the keyring.
Check with `gpg --list-secret-keys`.

**`gpg: signing failed: Inappropriate ioctl for device`**
The GPG agent is trying to open a pinentry dialog in a no-terminal environment.
Add `-Dgpg.pinentry-mode=loopback` to the `mvn deploy` command (already done
in `ci.yml`).

**`gpg: keyserver send failed: No route to host`**
The keyserver is temporarily unreachable. Try an alternative:
```bash
gpg --keyserver keys.openpgp.org --send-keys 3AA5C34371567BD2
```

**Maven Central rejects the release: signature cannot be verified**
The public key was not uploaded to a keyserver, or it has not propagated yet
(can take up to 10 minutes). Re-run Step 4 and wait before retrying the release.
