package com.tgad.reporter.dependents

import com.tgad.reporter.ProjectNode

class HtmlRenderer {

    fun render(rootNode: ProjectNode, nameStartNode: String): String {
        val title = "Dependents Tree for $nameStartNode"

        val body = buildString {
            appendLine("<h1>$title</h1>")

            appendLine("""
            <div class="toolbar">
              <button id="expandAll">Expand all</button>
              <button id="collapseAll">Collapse all</button>
            </div>
        """.trimIndent())

            appendLine("<div class='tree'>")
            appendLine("<ul>")
            append(renderNode(rootNode, isRoot = true))
            appendLine("</ul>")
            appendLine("</div>")
        }

        return wrapHtml(title, body)
    }

    private fun renderNode(node: ProjectNode, isRoot: Boolean): String {
        val label = escape("${node.name}${if (node.isCycleTerminal) " (cycle)" else ""}")

        return if (node.children.isNotEmpty()) {
            """
        <li>
          <details ${if (isRoot) "open" else ""}>
            <summary>$label</summary>
            <ul>
              ${node.children.joinToString("\n") { renderNode(it, isRoot = false) }}
            </ul>
          </details>
        </li>
        """.trimIndent()
        } else {
            if (node.isCycleTerminal) {
                """<li><span class="cycle" title="Cycle terminates here">🔁 $label</span></li>"""
            } else {
                """<li><span class="leaf">$label</span></li>"""
            }
        }
    }


    private fun wrapHtml(title: String, body: String): String = """
        <!doctype html>
        <html lang="en">
        <head>
          <meta charset="utf-8"/>
          <meta name="viewport" content="width=device-width, initial-scale=1"/>
          <title>${escape(title)}</title>
          <style>
            :root { --bg:#0b1020; --card:#121832; --text:#e8ecff; --muted:#9aa3c7; --accent:#7aa2ff; --warn:#ffb454; }
            html, body { background: var(--bg); color: var(--text); font: 14px/1.45 system-ui, -apple-system, Segoe UI, Roboto, sans-serif; }
            h1 { font-size: 20px; margin: 16px 0 8px; }
            p { color: var(--muted); margin: 0 0 16px; }
            .toolbar { position: sticky; top: 0; display: flex; gap: 8px; padding: 8px; background: linear-gradient(180deg, rgba(11,16,32,0.95), rgba(11,16,32,0.85)); backdrop-filter: blur(6px); }
            button { background: var(--card); color: var(--text); border: 1px solid #223; border-radius: 10px; padding: 6px 10px; cursor: pointer; }
            button:hover { border-color: var(--accent); }
            .tree { background: var(--card); border: 1px solid #223; border-radius: 14px; padding: 12px 16px; }
            details { margin: 4px 0; }
            summary { cursor: pointer; outline: none; }
            ul { list-style: none; padding-left: 18px; margin: 4px 0; border-left: 1px dashed #334; }
            .leaf { color: var(--text); opacity: 0.95; }
            .cycle { color: var(--warn); font-weight: 600; }
            .muted { color: var(--muted); }
            code { background: #0b0f1f; border: 1px solid #223; border-radius: 8px; padding: 2px 6px; }
          </style>
        </head>
        <body>
          <main style="max-width: 980px; margin: 24px auto; padding: 0 12px;">
            $body
          </main>
          <script>
            // Expand/Collapse all
            document.getElementById('expandAll')?.addEventListener('click', () => {
              document.querySelectorAll('details').forEach(d => d.open = true);
            });
            document.getElementById('collapseAll')?.addEventListener('click', () => {
              document.querySelectorAll('details').forEach(d => d.open = false);
            });
          </script>
        </body>
        </html>
    """.trimIndent()

    private fun escape(s: String): String =
        s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
}
