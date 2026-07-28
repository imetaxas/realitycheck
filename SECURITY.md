# Security Policy

## Supported Versions

Only the latest release receives security patches.

| Version | Supported |
|---------|-----------|
| 1.x (latest) | ✅ |
| < 1.0 | ❌ |

## Reporting a Vulnerability

**Please do not open a public GitHub issue for security vulnerabilities.**

Report security issues by emailing **imetaxas@gmail.com** with the subject line:

```
[realitycheck] Security Vulnerability Report
```

Include:

- A description of the vulnerability and its potential impact
- Steps to reproduce or a minimal proof-of-concept
- The affected version(s)
- Any suggested fix, if you have one

You will receive an acknowledgement within **72 hours**. If the issue is confirmed, a patch will be released as quickly as possible — typically within 7 days for critical issues, 30 days for lower severity.

We ask that you give us reasonable time to address the issue before any public disclosure.

## Scope

Reality Check is a **test-scope** library — it is not intended to run in production systems. However, the following are still considered in scope:

- Dependency vulnerabilities in transitive test dependencies
- Unsafe deserialization or arbitrary code execution via crafted inputs (e.g. in snapshot testing or CSV/XML/YAML parsers)
- Information disclosure through failure messages

## Out of Scope

- Vulnerabilities that only affect code explicitly annotated `@Deprecated(forRemoval = true)`
- Issues in test utilities that cannot realistically be exploited in any deployment

## Acknowledgements

We will publicly credit researchers who responsibly disclose valid vulnerabilities (unless they prefer to remain anonymous).
