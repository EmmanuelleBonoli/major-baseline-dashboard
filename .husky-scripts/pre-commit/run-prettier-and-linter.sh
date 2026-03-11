source "$(dirname "$0")/../utils.sh"

echo
echo_separator_general
echo_yellow "⚡ Running Checkstyle & Prettier for projects..."
echo_separator_general

echo_yellow "➡️ Inspecting Backend..."
cd back-end || exit 1
if mvn checkstyle:check; then
    echo_green "✅ Back-end Checkstyle passed"
else
    echo_separator
    echo_red "⚠️ Error while running Checkstyle (back-end). Please fix the errors."
    echo_separator
    exit 1
fi
cd ..

echo
echo_separator_general
echo_yellow "➡️ Inspecting Frontend..."
cd front-end || exit 1

# Since there's often ESLint and typechecking, we run npm run build or npm run lint if configured.
# We'll just run Prettier for now based on the original script, or a specific npm script if it exists:
if npx prettier --write "src/**/*.{js,vue,ts,css}"; then
    echo_green "✅ Front-end Prettier passed"
else
    echo_separator
    echo_red "⚠️ Error while running Prettier (front-end). Please fix the errors."
    echo_separator
    exit 1
fi
cd ..
