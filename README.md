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


Also...change the layout to any other

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
