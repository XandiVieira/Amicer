# Amicer
Amicer
Código para posível correção de erro:

I had found the solution for this
plz update your code
`public GhostHeaderViewHolder onCreateGhostHeaderViewHolder(ViewGroup parent) {
LayoutInflater inflater = LayoutInflater.from(parent.getContext());

View v = inflater.inflate(R.layout.ghost_view, parent, false);
return new GhostHeaderViewHolder(v);
}`

Please add an empty layout to onCreateGhostHeaderViewHolder
