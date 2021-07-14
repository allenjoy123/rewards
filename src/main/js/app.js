'use strict';

// tag::vars[]
const React = require('react'); // <1>
const ReactDOM = require('react-dom'); // <2>
const client = require('./client'); // <3>
const follow = require('./follow'); // function to hop multiple links by "rel"
const root = '/api';
// end::vars[]

// tag::app[]
class App extends React.Component { // <1>

	constructor(props) {
		super(props);
		this.state = {customers: [], purchases: []};
	}

	componentDidMount() { // <2>
		client({method: 'GET', path: '/api/customers'}).done(response => {
			this.setState({customers: response.entity._embedded.customers});
		});
		follow(client, root, [
			{rel: 'purchases'}]
		).then(purchaseCollection => {
			return client({
				method: 'GET',
				path: purchaseCollection.entity._links.products.href,
				headers: {'Accept': 'application/json'}
			}).then(productCollection => {
				// tag::json-schema-filter[]

				this.links = productCollection.entity._links;
				purchaseCollection.entity._embedded.purchases.productCollection = productCollection;
				return purchaseCollection;
				// end::json-schema-filter[]
			});
		}).done(purchaseCollection => {
			debugger;
			this.setState({
				page: this.page,
				purchases: purchaseCollection.entity._embedded.purchases,
				attributes: Object.keys(this.schema.properties),
				links: this.links
			});
		});
	}

	render() { // <3>
		return (
			<React.Fragment>
				<PurchaseList purchases={this.state.purchases}/>
				<CustomerList customers={this.state.customers}/>
			</React.Fragment>

		)
	}
}
// end::app[]

// tag::purchase-list[]
class PurchaseList extends React.Component{
	render() {
		const purchases = this.props.purchases.map(purchase =>
			<Purchase key={purchase._links.self.href} purchase={purchase}/>
		);

		return (
			<table>
				<tbody>
				<tr>
					<th>Purchase Date</th>
					<th>Product</th>
				</tr>
				{purchases}
				</tbody>
			</table>
		)
	}
}
// end::purchase-list[]

// tag::customer-list[]
class CustomerList extends React.Component{
	render() {
		const customers = this.props.customers.map(customer =>
			<Customer key={customer._links.self.href} customer={customer}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Description</th>
					</tr>
					{customers}
				</tbody>
			</table>
		)
	}
}
// end::customer-list[]

// tag::purchase[]
class Purchase extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.purchase.purchaseDate}</td>
				<td>{this.props.purchase.products}</td>

			</tr>
		)
	}
}
// end::purchase[]

// tag::customer[]
class Customer extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.customer.firstName}</td>
				<td>{this.props.customer.lastName}</td>
				<td>{this.props.customer.description}</td>
			</tr>
		)
	}
}
// end::customer[]

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
)
// end::render[]
