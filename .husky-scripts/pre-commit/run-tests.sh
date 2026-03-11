source "$(dirname "$0")/../utils.sh"

echo
echo_separator_general
echo_yellow "⚡ Running tests..."
echo_separator_general

echo_yellow "➡️ Testing Backend..."
cd back-end || exit 1
if mvn test; then
    echo_green "✅ Back-end tests passed"
else
    echo_separator
    echo_red "⚠️ Error while running tests (back-end). Please fix the errors."
    echo_separator
    exit 1
fi
cd ..

echo
echo_separator_general
echo_yellow "➡️ Frontend Tests / Checks"
cd front-end || exit 1
# Optionally, if the frontend has tests (vitest etc) you'd run them here. 
# We'll run a build to check to make sure it compiles for now (typecheck)
if npm run build; then
    echo_green "✅ Front-end build/checks passed"
else
    echo_separator
    echo_red "⚠️ Error while running tests/build (front-end). Please fix the errors."
    echo_separator
    exit 1
fi
cd ..
