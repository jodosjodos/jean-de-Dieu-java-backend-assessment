# ⚡ Quick Command Reference - Feature PRs

## 🎯 PR #1: User Service (START NOW!)

### Step 1: Create Foundation on Main Branch

```bash
cd /home/jodos/Documents/codes/CHALLENGES/AMALI/project1/jean-de-Dieu-java-backend-assessment

# Add only foundation files
git add .gitignore
git add pom.xml
git add README.md

# Commit foundation
git commit -m "chore: Initialize project structure

- Add Maven parent POM with multi-module configuration
- Add .gitignore for Java/Maven projects
- Add README with project overview and setup instructions"

# NOW: Create GitHub repo at https://github.com/new
# Repository name: jean-de-Dieu-java-backend-assessment
# Make it PRIVATE
# DON'T add README, .gitignore, or license

# Add remote (replace YOUR_USERNAME with your GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/jean-de-Dieu-java-backend-assessment.git

# Push main branch
git push -u origin main
```

---

### Step 2: Create Feature Branch for User Service

```bash
# Create feature branch
git checkout -b feature/user-service

# Add common-utils (required dependency)
git add shared/common-utils/

# Commit common-utils
git commit -m "feat(shared): Add common utilities module

- Add ApiResponse and PageResponse DTOs for standardized API responses
- Add UserRole, IncidentStatus, IncidentPriority enums
- Add custom exceptions (BusinessException, ValidationException, ResourceNotFoundException)
- Add utility classes (CorrelationIdGenerator, DateTimeUtil, MDCUtil)
- Configure as shared Maven module for reuse across services"

# Add user-service
git add services/user-service/

# Commit user-service
git commit -m "feat(user-service): Implement authentication and user management

Features:
- JWT token generation and validation (access + refresh tokens)
- User registration with email validation
- Login endpoint returning JWT tokens
- Role-based access control (ADMIN, OPERATOR, SUPPORT)
- Spring Security configuration with BCrypt password hashing
- Liquibase migrations for users and refresh_tokens tables
- Swagger/OpenAPI documentation
- Unit tests for AuthService and AuthController
- PostgreSQL database integration
- Prometheus metrics and structured JSON logging

Tech Stack:
- Spring Boot 3.2.2
- Spring Security + JWT (JJWT 0.12.5)
- PostgreSQL 15 + Liquibase
- SpringDoc OpenAPI 3"

# Push feature branch
git push -u origin feature/user-service
```

---

### Step 3: Create Pull Request on GitHub

**Go to**: https://github.com/YOUR_USERNAME/jean-de-Dieu-java-backend-assessment

**Click**: "Compare & pull request" (yellow banner appears)

**OR**: Pull requests tab → New pull request → `main` ← `feature/user-service`

**Title**:
```
feat(user-service): Implement authentication and user management
```

**Description**: Copy from `FEATURE_PR_GUIDE.md` (the detailed PR description)

**Reviewers**: Add `gniyonge3` and `kevin-rukundo`

**Click**: "Create pull request"

---

## 📊 After PR #1 is Created

### Your PR will show:
- ✅ Files changed (shared/common-utils + services/user-service)
- ✅ CI pipeline running (build + tests)
- ✅ Ready for review

### Reviewers can:
- 💬 Comment on code
- ✅ Approve or request changes
- 🔀 Merge when approved

---

## 🔄 After PR #1 is Merged

### Step 4: Prepare for PR #2 (Incident Service)

```bash
# Switch back to main
git checkout main

# Pull merged changes
git pull origin main

# Delete local feature branch (optional, keeps things clean)
git branch -d feature/user-service

# Create next feature branch
git checkout -b feature/incident-service

# Add incident service
git add services/incident-service/

# Commit
git commit -m "feat(incident-service): Implement incident management with Kafka

Features:
- CRUD operations for incidents
- Incident lifecycle management (OPEN → IN_PROGRESS → RESOLVED → CLOSED)
- Priority levels (LOW, MEDIUM, HIGH, CRITICAL)
- Kafka producer for incident events (incident-created, incident-assigned, incident-resolved)
- Liquibase migrations for incidents and incident_audit tables
- PostgreSQL database integration
- Unit tests for IncidentService
- Swagger/OpenAPI documentation

Tech Stack:
- Spring Boot 3.2.2
- Apache Kafka 3.6.1
- PostgreSQL 15 + Liquibase
- Spring Data JPA"

# Push
git push -u origin feature/incident-service

# Create PR #2 on GitHub
```

---

## 📋 PR Sequence Checklist

- [ ] **PR #1**: User Service + Common Utils ⭐ (START HERE)
  - Files: `shared/common-utils/`, `services/user-service/`
  
- [ ] **PR #2**: Incident Service
  - Files: `services/incident-service/`
  
- [ ] **PR #3**: Notification Service
  - Files: `services/notification-service/`
  
- [ ] **PR #4**: Reporting + API Gateway + Config
  - Files: `services/reporting-service/`, `gateway/api-gateway/`, `config/config-service/`
  
- [ ] **PR #5**: Observability Stack
  - Files: `deployment/`, `docker-compose.yml`
  
- [ ] **PR #6**: Testing + CI/CD + Documentation
  - Files: `.github/`, `docs/`, `*.md` files

---

## 🚫 Common Mistakes to Avoid

### ❌ DON'T:
- Don't commit everything at once
- Don't push to main directly (after initial foundation)
- Don't create PR from main to main
- Don't merge your own PRs (wait for reviewer approval)
- Don't delete feature branch before merging

### ✅ DO:
- Create feature branch for each PR
- Commit related files together
- Write clear commit messages
- Push feature branch to GitHub
- Create PR from feature branch to main
- Wait for approval before merging
- Pull main after merge before next feature

---

## 💡 Quick Tips

### Check what branch you're on:
```bash
git branch
# The one with * is current
```

### See what's staged:
```bash
git status
```

### Undo staging (before commit):
```bash
git reset HEAD <file>
```

### See commit history:
```bash
git log --oneline
```

### Switch branches:
```bash
git checkout branch-name
```

---

## 📞 Need Help?

### If you mess up:
```bash
# Undo last commit (keeps changes)
git reset --soft HEAD~1

# Undo all changes (DANGEROUS!)
git reset --hard HEAD

# Switch branch (uncommitted changes will follow you)
git checkout other-branch

# Stash changes temporarily
git stash
git checkout other-branch
git stash pop
```

---

## ✅ Ready to Start!

**Copy and run these commands now**:

```bash
# 1. Commit foundation
git add .gitignore pom.xml README.md
git commit -m "chore: Initialize project structure"

# 2. Create GitHub repo (do on GitHub.com)

# 3. Add remote and push
git remote add origin https://github.com/YOUR_USERNAME/jean-de-Dieu-java-backend-assessment.git
git push -u origin main

# 4. Create feature branch
git checkout -b feature/user-service

# 5. Add and commit user service
git add shared/common-utils/
git commit -m "feat(shared): Add common utilities module"

git add services/user-service/
git commit -m "feat(user-service): Implement authentication and user management"

# 6. Push feature branch
git push -u origin feature/user-service

# 7. Go to GitHub and create PR!
```

**That's it! Your first PR will be ready for review!** 🚀
